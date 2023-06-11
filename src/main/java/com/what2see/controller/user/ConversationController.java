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

@RequiredArgsConstructor
@RestController
@RequestMapping("/conversation")
public class ConversationController {

    private final ConversationService conversationService;

    private final ConversationDTOMapper conversationMapper;

    private final MessageService messageService;

    private final MessageDTOMapper messageMapper;

    private final UserService<Tourist> touristService;

    private final UserService<Guide> guideService;

    // -------
    // GET
    // -------

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

    @GetMapping()// by conversation id or guide id, can return null if not existing yet
    public ResponseEntity<ConversationResponseDTO> getById(@RequestParam(required = false) Long conversationId, @RequestParam(required = false) Long guideId, @RequestHeader(value="Authentication") Long userId) {
        try {
            touristService.findById(userId);
        } catch (NoSuchElementException e) {
            guideService.findById(userId);
        }

        if(conversationId != null) {
            Conversation c = conversationService.findById(conversationId);
            if(!conversationService.isVisible(c, userId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a visualizzare questa conversazione");
            }
            return ResponseEntity.ok(conversationMapper.convertResponse(c));
        } else if(guideId != null) {    // is tourist get by guide id, if exists
            try {
                Conversation c = conversationService.findByParticipants(userId, guideId);
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

    @PostMapping("/message")
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody @Valid MessageCreateDTO m, @RequestHeader(value="Authentication") Long userId) {
        boolean direction;
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
