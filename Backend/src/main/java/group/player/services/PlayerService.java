package group.player.services;

import group.player.entities.Player;
import group.player.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayers(){
        return playerRepository.findAll(Sort.by(Sort.Order.desc("score")));
    }

    public void addPlayer(Player player) {
        playerRepository.save(player);
    }
}
