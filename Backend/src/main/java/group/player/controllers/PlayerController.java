package group.player.controllers;

import group.player.ListMapper;
import group.player.dto.PlayerDTO;
import group.player.entities.Player;
import group.player.services.PlayerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin({"http://127.0.0.1:5173"})
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public List<PlayerDTO> getAllPlayer() {
        List<Player> players = playerService.getAllPlayers();
        return listMapper.mapList(players, PlayerDTO.class, modelMapper);
    }

    @PostMapping("")
    public ResponseEntity<String> addPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = modelMapper.map(playerDTO, Player.class);
        playerService.addPlayer(player);
        return new ResponseEntity<>("Player added successfully", HttpStatus.CREATED);
    }
}
