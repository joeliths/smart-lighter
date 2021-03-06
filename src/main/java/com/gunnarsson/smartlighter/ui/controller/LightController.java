package com.gunnarsson.smartlighter.ui.controller;

import com.gunnarsson.smartlighter.service.LightService;
import com.gunnarsson.smartlighter.service.PresetService;
import com.gunnarsson.smartlighter.shared.dto.CollectionPresetDto;
import com.gunnarsson.smartlighter.shared.dto.LightDto;
import com.gunnarsson.smartlighter.shared.dto.PresetDto;
import com.gunnarsson.smartlighter.ui.model.request.LightRequestModel;
import com.gunnarsson.smartlighter.ui.model.request.LightStateModel;
import com.gunnarsson.smartlighter.ui.model.request.PresetLightsRequestModel;
import com.gunnarsson.smartlighter.ui.model.response.LightResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lights")
public class LightController {
    @Autowired
    LightService lightService;

    @Autowired
    PresetService presetService;

    @GetMapping
    public List<LightResponseModel> getAllLights(){
        List<LightDto> lightDtoList = lightService.getAllLights();
        Type listType = new TypeToken<List<LightResponseModel>>(){}.getType();
        return new ModelMapper().map(lightDtoList,listType);
    }

    @GetMapping(path = "on")
    public String turnOnLight(@RequestParam String lightId){
        return lightService.turnOn(lightId);
    }

    @PostMapping
    public LightResponseModel createLight(@RequestBody LightRequestModel lightRequestModel){

        LightDto lightDto = new ModelMapper().map(lightRequestModel,LightDto.class);
        LightDto createdLight = lightService.createLight(lightDto);
        return new ModelMapper().map(createdLight,LightResponseModel.class);
    }

    @PostMapping(path = "preset",consumes= MediaType.APPLICATION_JSON_VALUE)
    public CollectionPresetDto createPreset (@RequestBody PresetLightsRequestModel presetLightsRequestModel){
        CollectionPresetDto collectionPresetDto = new CollectionPresetDto();
        // move below to helpMethod
        List<PresetDto>presetDtos = new ArrayList<>();
        for(LightStateModel lightStateModel: presetLightsRequestModel.getPresets()){
             PresetDto presetDto = new PresetDto();
             presetDto.setOn(lightStateModel.isOn());
            presetDto.setLight(lightService.findLightByLightId(lightStateModel.getLightId()));
        }
        collectionPresetDto.setPresets(presetDtos);
        collectionPresetDto.setCollectionName(presetLightsRequestModel.getCollectionName());
        CollectionPresetDto createdCollectionPreset = presetService.createCollectionPreset(collectionPresetDto);
        return createdCollectionPreset;
    }
}
