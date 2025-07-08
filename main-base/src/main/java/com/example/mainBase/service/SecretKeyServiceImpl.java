package com.example.mainBase.service;

import com.example.mainBase.entities.SecretKey;
import com.example.mainBase.exception.ResourceNotFoundException;
import com.example.mainBase.repository.SecretKeyRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecretKeyServiceImpl implements SecretKeyService {

  private final SecretKeyRepository secretKeyRepository;

  @Override
  public String getKey() {
    Optional<SecretKey> key = secretKeyRepository.findById(1L);

    if(key.isPresent()) {
      return key.get().getKey();
    }else {
      throw new ResourceNotFoundException("Secret key not found");
    }
  }
}
