package com.dataart.project1.service.game;

import com.dataart.project1.entity.Mission;
import com.dataart.project1.entity.MissionStatus;
import com.dataart.project1.entity.MissionType;
import com.dataart.project1.entity.User;
import com.dataart.project1.entity.dto.MissionResultDto;
import com.dataart.project1.repo.MissionRepo;
import com.dataart.project1.repo.MissionTypeRepo;
import com.dataart.project1.repo.StarshipRepo;
import com.dataart.project1.service.game.battle.BattleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.dataart.project1.entity.MissionStatus.*;

@Service
public class MissionService {

    private static Logger logger = LoggerFactory.getLogger(MissionService.class);

    private SquadService squadService;
    private MissionTypeRepo missionTypeRepo;
    private MissionRepo missionRepo;
    private BattleService battleService;
    private StarshipRepo starshipRepo;


    @Autowired
    public MissionService(SquadService squadService, MissionTypeRepo missionTypeRepo,
                          MissionRepo missionRepo, BattleService battleService,
                          StarshipRepo starshipRepo) {
        this.squadService = squadService;
        this.missionTypeRepo = missionTypeRepo;
        this.missionRepo = missionRepo;
        this.battleService = battleService;
        this.starshipRepo = starshipRepo;
    }

    public void createMission(Long typeId, Long squadId, User user) {
        Mission newMission = new Mission();
        newMission.setStatus(MissionStatus.NEW_MISSION);
        missionRepo.save(newMission);
        var missionType = missionTypeRepo.findById(typeId).orElse(null);
        var squad = squadService.getSquadById(squadId, user);

        if (missionType == null || squad == null) {
            throw new RuntimeException("Cannot create new mission with provided params: typeId:" + typeId + " ,squadId: " + squadId);
        }

        squad.setActiveMission(newMission);
        newMission.setAssignedSquad(squad);
        newMission.setCreationTime(LocalDateTime.now());
        newMission.setEndTime(LocalDateTime.now().plus(missionType.getDuration(), ChronoUnit.SECONDS));
        newMission.setUser(user);
        newMission.setType(missionType);
        newMission.setStatus(MissionStatus.IN_PROGRESS);
        squadService.save(squad);
        missionRepo.save(newMission);
    }

    public void simulateMission(Mission mission) {
        if (!mission.getStatus().equals(IN_PROGRESS)) {
            logger.warn("Mission with id {} has wrong status {}", mission.getId(), mission.getStatus());
            return;
        }

        battleService.simulateBattle(mission);

        for (var ship : mission.getAssignedSquad().getShips()){
            if (ship.getHealth() <=0){
                starshipRepo.delete(ship);
            } else {
                starshipRepo.save(ship);
            }
        }
        missionRepo.save(mission);
    }

    public MissionResultDto completeMission(Mission mission) {
        MissionType type = mission.getType();
        var result = new MissionResultDto();
        if (mission.getAssignedSquad().getShips().stream().anyMatch(x -> x.getHealth() > 0)) {
            double missionPay = type.getReward();

            Random rnj = new Random();
            boolean giveCores = rnj.nextFloat() <= mission.getType().getRewardCoresChance();
            int coreAmount = 0;
            if (mission.getType().getRewardCoresMax() > 0){
                coreAmount = mission.getType().getRewardCoresMin() + Math.round(rnj.nextInt(mission.getType().getRewardCoresMax()));
                if (giveCores){
                    mission.getUser().setAiCores(BigInteger.valueOf(mission.getUser().getAiCores().intValue() + coreAmount));
                }
            }

            result.setMissionCores(coreAmount);
            mission.setStatus(COMPLETED);
            mission.getUser().setCredits(mission.getUser().getCredits().add(new BigDecimal(missionPay)));
            result.setSquad(mission.getAssignedSquad());
            mission.getAssignedSquad().setActiveMission(null);
            mission.setAssignedSquad(null);
            missionRepo.save(mission);
            result.setIsSuccessful(true);
            result.setMissionPay(BigDecimal.valueOf(missionPay));
        } else { // all ships were destroyed!
            mission.setStatus(FAILED);
            result.setSquad(mission.getAssignedSquad());
            mission.getAssignedSquad().setActiveMission(null);
            mission.setAssignedSquad(null);
            missionRepo.save(mission);
            result.setIsSuccessful(false);
            result.setMissionPay(BigDecimal.ZERO);
            result.setMissionCores(0);
        }
        return result;
    }

    public Set<MissionType> getMissionTypes(User currentUser) {
        // might filter by available to user here
        return new HashSet<>(missionTypeRepo.findAll());
    }

    public Set<Mission> getUserMissions(User currentUser) {
        return missionRepo.findAllByUser(currentUser);
    }


    public Set<Mission> fetchAndUpdateMissions(User currentUser) {
        Set<Mission> allByUser = missionRepo.findAllByUserAndStatusIsLikeInProgress(currentUser);
        allByUser.stream().filter(mission -> mission.getEndTime().isBefore(LocalDateTime.now()))
                .forEach(mission -> {
//                    mission.setStatus(MissionStatus.FINISHED);
                    missionRepo.save(mission);
                });
        return allByUser;
    }

    public Mission getMissionById(Long missionId) {
        return missionRepo.findById(missionId).orElse(null);
    }

    public MissionType getMissionTypeById(Long missionId) {
        return missionTypeRepo.findById(missionId).orElse(null);
    }

}


