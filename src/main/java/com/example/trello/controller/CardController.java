package com.example.trello.controller;
import com.example.trello.dto.ApiResponseDto;
import com.example.trello.dto.CardRequestDto;
import com.example.trello.dto.CardResponseDto;
import com.example.trello.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;
    // 카드 조회 -> Column 에서 List 로 조회

    // 카드 생성
    @PostMapping("/{columnId}/card")
    public ResponseEntity<CardResponseDto> createCard(@PathVariable Long columnId, @RequestBody CardRequestDto cardRequestDto) {
        CardResponseDto cardResponseDto = cardService.createCard(columnId, cardRequestDto);
        return new ResponseEntity<>(
                cardResponseDto,
                HttpStatus.CREATED
        );
    }

    // 카드 수정
    @PutMapping("/card/{id}")
    public ResponseEntity<ApiResponseDto> updateCard(@PathVariable Long id, @RequestBody CardRequestDto cardRequestDto) {
        cardService.updateCard(id, cardRequestDto);
        ApiResponseDto apiResponseDto = new ApiResponseDto("카드를 수정했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.OK
        );
    }

//    // 카드 수정 샘플
//    @PutMapping("/cards/{id}")
//    public ResponseEntity<ApiResponseDto> updateCard(@AuthenticationPrincipal UserDetailslmpl userDetails, @PathVariable Long id, @RequestBody CardRequestDto requestDto) {
//        try {
//            CardResponseDto result = cardService.updateCard(id, requestDto, userDetails.getUser());
//            return ResponseEntity.ok().body(result);
//        } catch (RejectedExecutionException e) {
//            return ResponseEntity.badRequest().body(new ApiResponseDto("수정할 수 있는 권한이 없습니다.", HttpStatus.BAD_REQUEST.value()));
//        }
//    }

    // 카드 삭제
    @DeleteMapping("/card/{id}")
    public ResponseEntity<ApiResponseDto> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        ApiResponseDto apiResponseDto = new ApiResponseDto("카드를 삭제했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.OK
        );
    }

//    // 카드 삭제 샘플
//    @DeleteMapping("/cards/{id}")
//    public ResponseEntity<ApiResponseDto> deletePost(@AuthenticationPrincipal UserDetailslmpl userDetails, @PathVariable Long id) {
//        try {
//            cardService.deleteCard(id, userDetails.getUser());
//            return ResponseEntity.ok().body(new ApiResponseDto("카드를 삭제하였습니다.", HttpStatus.OK.value()));
//        } catch (RejectedExecutionException e) {
//            return ResponseEntity.ok().body(new ApiResponseDto("카드를 삭제 할 수 있는 권한이 없습니다.",HttpStatus.BAD_REQUEST.value()));
//        }
//    }
}
