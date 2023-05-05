package com.what2see.controller.user;

import com.what2see.dto.user.ConversationResponseDTO;
import com.what2see.mapper.user.ConversationDTOMapper;
import com.what2see.model.user.Conversation;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.service.user.ConversationService;
import com.what2see.service.user.GuideService;
import com.what2see.service.user.TouristService;
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

    private final TouristService touristService;

    private final GuideService guideService;


    @GetMapping()
    public ResponseEntity<List<ConversationResponseDTO>> getAll(@RequestHeader(value="Authentication") Long userId) {
        // FIXME using workaround for allowing multiple roles
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

    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationResponseDTO> getById(@PathVariable Long conversationId, @RequestHeader(value="Authentication") Long userId) {
        Conversation c = conversationService.findById(conversationId);
        if(!conversationService.checkVisibility(c, userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a visualizzare questa conversazione");
        }
        return ResponseEntity.ok(conversationMapper.convertResponse(c));
    }

}
