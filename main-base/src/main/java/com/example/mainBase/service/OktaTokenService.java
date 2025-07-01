package com.example.mainBase.service;

public interface OktaTokenService {

  String getAccessToken();

  void fetchNewToken();

}
