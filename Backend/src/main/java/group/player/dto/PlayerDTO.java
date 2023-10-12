package group.player.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;


@Data
public class PlayerDTO implements Serializable {
    private String name;
    private Integer score;
    private ZonedDateTime date;
}