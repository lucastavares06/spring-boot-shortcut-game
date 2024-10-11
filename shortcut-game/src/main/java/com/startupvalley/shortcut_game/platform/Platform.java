package com.startupvalley.shortcut_game.platform;

import com.startupvalley.shortcut_game.game.Game;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "platforms")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "games") // Evita ciclo de referência
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "platforms")
    private List<Game> games;
}
