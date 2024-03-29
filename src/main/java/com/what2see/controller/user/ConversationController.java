package com.what2see.controller.user;

import com.what2see.dto.user.ConversationCreateDTO;
import com.what2see.dto.user.ConversationResponseDTO;
import com.what2see.dto.user.MessageCreateDTO;
import com.what2see.dto.user.MessageResponseDTO;
import com.what2see.exception.ConversationAlreadyStartedException;
import com.what2see.mapper.user.ConversationDTOMapper;
import com.what2see.mapper.user.MessageDTOMapper;
import com.what2see.model.user.Conversation;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Message;
import com.what2see.model.user.Tourist;
import com.what2see.service.user.ConversationService;
import com.what2see.service.user.MessageService;
import com.what2see.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controller for conversions and messages endpoints
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/conversation")
public class ConversationController {

    // dependencies autowired by spring boot

    private final ConversationService conversationService;

    private final ConversationDTOMapper conversationMapper;

    private final MessageService messageService;

    private final MessageDTOMapper messageMapper;

    private final UserService<Tourist> touristService;

    private final UserService<Guide> guideService;


    /*
     * Some validations are not explicitly performed with try/catch's since RuntimeExceptions are expected to
     * be called and managed from the Spring Boot container in case of failed validation or user not found.
     */

    // -------
    // GET
    // -------

    /**
     * Get all conversations of the requesting user (tourist or guide)
     * @param userId requesting user (must be a tourist or a guide)
     * @return list of conversations (DTO alternative without info about messages for performance reasons)
     * @see ConversationResponseDTO#messages
     */
    @GetMapping("/all")
    public ResponseEntity<List<ConversationResponseDTO>> getAll(@RequestHeader(value="Authentication") Long userId) {
        List<Conversation> c;
        try {
            Tourist t = touristService.findById(userId);
            c = t.getConversations();
        } catch (NoSuchElementException e) {
            Guide g = guideService.findById(userId);
            c = g.getConversations();
        }
        return ResponseEntity.ok(conversationMapper.convertResponseLight(c));
    }

    /**
     * Get a specific conversation (if already started) by its id <i>or</i>, indirectly, by guide id (only for tourists).<br>
     * It is intended to only be used by one of the conversation participants (tourist or guide).
     * @param conversationId conversation to get <i>or</i>
     * @param guideId guide to get conversation with (only for tourists)
     * @param userId requesting user (must be a tourist or a guide and one of the conversation participants)
     * @return wanted conversation or empty response if not started yet
     * @exception ResponseStatusException {@link HttpStatus#UNAUTHORIZED} if user is not authorized to see the wanted conversation
     */
    @GetMapping()// by conversation id or guide id, can return null if not existing yet
    public ResponseEntity<ConversationResponseDTO> getById(@RequestParam(required = false) Long conversationId, @RequestParam(required = false) Long guideId, @RequestHeader(value="Authentication") Long userId) {
        try {
            touristService.findById(userId);
        } catch (NoSuchElementException e) {
            guideService.findById(userId);
        }

        if(conversationId != null) { // is tourist or guide get by conversation id
            Conversation c = conversationService.findById(conversationId);
            if(!conversationService.isVisible(c, userId)) { // ensure user can actually see the wanted conversation
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a visualizzare questa conversazione");
            }
            return ResponseEntity.ok(conversationMapper.convertResponse(c));
        } else if(guideId != null) {    // is tourist get by guide id, if exists
            try {
                Conversation c = conversationService.findByParticipants(userId, guideId);   // if not existing yet, NoSuchElementException is thrown
                return ResponseEntity.ok(conversationMapper.convertResponse(c));
            } catch (NoSuchElementException e) {
                return ResponseEntity.noContent().build();
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // -------
    // POST
    // -------

    /**
     * Start a new conversation with a guide.<br>
     * It is intended to only be used by tourists and only if the conversation has not been already started.
     * @param c conversation to start with at least one message
     * @param touristId requesting tourist
     * @return created conversation
     * @exception ResponseStatusException {@link HttpStatus#BAD_REQUEST} if conversation has been already started
     */
    @PostMapping()
    public ResponseEntity<ConversationResponseDTO> startConversation(@RequestBody @Valid ConversationCreateDTO c, @RequestHeader(value="Authentication") Long touristId) {
        touristService.findById(touristId);
        Conversation createdConversation;
        try {
            createdConversation = conversationService.startConversation(conversationMapper.convertCreate(c, touristId));
        } catch (ConversationAlreadyStartedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esiste già una conversazione con questa guida");
        }
        return ResponseEntity.ok(conversationMapper.convertResponse(createdConversation));
    }

    /**
     * Send a message in an existing conversation.<br>
     * It is intended to only be used by one of the conversation participants (tourist or guide).<br>
     * Note that {@link Message#direction} boolean is automatically set, based on the requesting user role.
     * @param m message to send (including conversation id)
     * @param userId requesting user (must be a tourist or a guide and one of the conversation participants)
     * @return created message
     * @exception ResponseStatusException {@link HttpStatus#UNAUTHORIZED} if user is not authorized to send a message in the wanted conversation
     */
    @PostMapping("/message")
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody @Valid MessageCreateDTO m, @RequestHeader(value="Authentication") Long userId) {
        boolean direction;  // true if guide, false if tourist
        try {
            touristService.findById(userId);
            direction = false;
        } catch (NoSuchElementException e) {
            guideService.findById(userId);
            direction = true;
        }

        Conversation c = conversationService.findById(m.getConversationId());
        if(!conversationService.isVisible(c, userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a inviare un messaggio in questa conversazione");
        }

        Message createdMessage = messageService.sendMessage(messageMapper.convertCreate(m, direction));
        return ResponseEntity.ok(messageMapper.convertResponse(createdMessage));
    }

}
