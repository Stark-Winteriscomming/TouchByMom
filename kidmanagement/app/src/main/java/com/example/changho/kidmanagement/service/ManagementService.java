package com.example.changho.kidmanagement.service;

/**
 * Created by Changho on 2016-10-31.
 */

/**
 *
 * 부모-아이 사이의 서비스 수행
 *
 */
public interface ManagementService {
    // 아이의 최신 상태정보 가져오기
    void synkChildAppState();

    // appname packagename jsonString
    void getAppInfo();
}
