package com.dataart.project1.service.game;

import com.dataart.project1.entity.Squad;
import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.User;
import com.dataart.project1.repo.SquadRepo;
import com.dataart.project1.repo.StarshipRepo;
import com.dataart.project1.service.game.battle.Fleet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SquadService {

    private SquadRepo squadRepo;
    private StarshipRepo starshipRepo;

    @Autowired
    public SquadService(SquadRepo squadRepo, StarshipRepo starshipRepo) {
        this.squadRepo = squadRepo;
        this.starshipRepo = starshipRepo;
    }

    public List<Squad> getSquadsForUser(User user) {
        return squadRepo.findAllByOwner(user);
    }

    public List<Squad> getAvailableSquadsForUser(User user) {
        return squadRepo.findAllByOwnerAndActiveMissionIsNullAndShipsIsNotEmpty(user);
    }

    public SquadSummary getSummary(Squad squad) {
        var fleet = new Fleet(squad.getShips());
        fleet.updateDpsMap();
        var summary = new SquadSummary();
        summary.setSquad(squad);
        summary.setDpsMap(fleet.getDpsMap());
        return summary;
    }

    public Squad getSquadByName(String name, User user) {
        return squadRepo.findByNameAndOwner(name, user);
    }

    public Squad getSquadById(Long id, User user) {
        return squadRepo.findByIdAndOwner(id, user);
    }

    @Transactional
    public void updateSquad(Squad squad, Set<Long> squadShips) {
        Set<Starship> newSquadShips = squadShips.stream()
                .map(x -> starshipRepo.findById(Long.valueOf(x)).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        squad.getShips().forEach(x -> {
            if (!squadShips.contains(x.getId())) {
                x.setSquad(null);
            }
        });

        newSquadShips.forEach(x -> x.setSquad(squad));
        squad.setShips(newSquadShips);
    }

    @Transactional
    public void disbandSquad(Squad squad) {
        for (Starship ship : squad.getShips()) {
            ship.setSquad(null);
        }
        squadRepo.delete(squad);
    }

    public void save(Squad newSquad) {
        squadRepo.save(newSquad);
    }
}
