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


    @GetMapping()// can return a list (getAll), a specific converstation (getById) or null if not existing yet
    public ResponseEntity<?> get(@RequestParam(required = false) Long conversationId, @RequestParam(required = false) Long guideId, @RequestHeader(value="Authentication") Long userId) {
        if(conversationId != null) {    // is get by id
            Conversation c = conversationService.findById(conversationId);
            if(!conversationService.checkVisibility(c, userId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a visualizzare questa conversazione");
            }
            return ResponseEntity.ok(conversationMapper.convertResponse(c));
        } else if(guideId != null) {    // is tourist get by guide id, if exists
            Conversation c = conversationService.findByParticipants(userId, guideId);
            if(c != null) {
                return ResponseEntity.ok(conversationMapper.convertResponse(c));
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {    // is get all
            // FIXME using workaround for allowing multiple roles
            List<Conversation> c;
            try {
                Tourist t = touristService.findById(userId).orElseThrow();
                c = t.getConversations();
            } catch (NoSuchElementException e) {
                Guide g = guideService.findById(userId).orElseThrow();
                c = g.getConversations();
            }
            return ResponseEntity.ok(conversationMapper.convertResponseLight(c));
        }
    }

    @PostMapping()
    public ResponseEntity<ConversationResponseDTO> startConversation(@RequestParam Long guideId, @RequestBody String message, @RequestHeader(value="Authentication") Long userId) {
        Conversation c;
        try {
            c = conversationService.startConversation(conversationMapper.convertCreate(ConversationCreateDTO.builder()
                .guideId(guideId)
                .touristId(userId)
                .message(message)
                .build()));
        } catch (ConversationAlreadyStartedException e) {
            c = conversationService.findByParticipants(userId, guideId);
        }
        return ResponseEntity.ok(conversationMapper.convertResponse(c));
    }

    @PostMapping("/{conversationId}/message")
    public ResponseEntity<MessageResponseDTO> sendMessage(@PathVariable Long conversationId, @RequestBody String message, @RequestHeader(value="Authentication") Long userId) {
        Conversation c = conversationService.findById(conversationId);
        if(!conversationService.checkVisibility(c, userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a inviare un messaggio in questa conversazione");
        }
        boolean direction;
        try {
            Tourist t = touristService.findById(userId).orElseThrow();
            direction = false;
        } catch (NoSuchElementException e) {
            Guide g = guideService.findById(userId).orElseThrow();
            direction = true;
        }
        Message m = messageService.sendMessage(messageMapper.convertCreate(MessageCreateDTO.builder()
            .direction(direction)
            .content(message)
            .conversationId(conversationId)
            .build()));
        return ResponseEntity.ok(messageMapper.convertResponse(m));
    }

}
