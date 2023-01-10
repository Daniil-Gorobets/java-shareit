package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(
            @Validated({Create.class}) @RequestBody ItemDto itemDto,
            @RequestHeader(USER_ID_HEADER) long owner) {
        return itemService.createItem(itemDto, owner);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @PathVariable long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader(USER_ID_HEADER) long owner) {
        return itemService.updateItem(itemId, owner, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoBooking getItem(
            @PathVariable long itemId,
            @RequestHeader(USER_ID_HEADER) long owner) {
        return itemService.getItem(itemId, owner);
    }

    @GetMapping
    public List<ItemDtoBooking> getItems(
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "10") int size,
            @RequestHeader(USER_ID_HEADER) long owner) {
        return itemService.getUserItems(owner, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam String text) {
        return itemService.findItem(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(
            @Validated({Create.class}) @RequestBody CommentDto commentDto,
            @PathVariable long itemId,
            @RequestHeader(USER_ID_HEADER) long owner) {
        return itemService.createComment(commentDto, itemId, owner);
    }
}
