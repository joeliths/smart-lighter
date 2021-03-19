package com.gunnarsson.smartlighter.service.impl;

import com.gunnarsson.smartlighter.io.entity.LightEntity;
import com.gunnarsson.smartlighter.io.repositories.LightRepository;
import com.gunnarsson.smartlighter.service.LightService;
import com.gunnarsson.smartlighter.service.command.impl.LightOnCommand;
import com.gunnarsson.smartlighter.shared.Utils;
import com.gunnarsson.smartlighter.shared.dto.LightDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LightServiceImpl implements LightService {

    @Autowired
    LightRepository lightRepository;

    @Autowired
    Utils utils;

    @Override
    public LightDto createLight(LightDto light) {
        LightEntity lightEntity = new ModelMapper().map(light,LightEntity.class);
        lightEntity.setLightId(utils.generateLightId(10));
        LightEntity savedLight = lightRepository.save(lightEntity);
        return new ModelMapper().map(savedLight,LightDto.class);

    }

    @Override
    public String turnOn(String lightId) {
       LightEntity light = lightRepository.findLightByLightId(lightId);
        LightDto lightDto = new ModelMapper().map(light,LightDto.class);
        return new LightOnCommand(lightDto).execute();
    }
}
