package com.alwertus.spassistent.parts.info.service;

import com.alwertus.spassistent.parts.info.model.Space;
import com.alwertus.spassistent.parts.info.view.CreateSpaceRequest;

import java.util.List;

public interface IInfoService {
    List<Space> getSpaces();
    void createSpace(CreateSpaceRequest rq);
}
