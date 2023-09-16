
![text-logo-ver2](https://github.com/Libienz/Spoot-Taxi-Server/assets/85234650/87329599-6b82-40a3-b892-3b1191950e55)


# 🚕Spoot Taxi Android Client

- 2023 Sangmyung University Capstone Project (졸업 작품)
- 택시 동승 파티 원클릭 매칭 안드로이드 플랫폼
- 💻 Socket Server: https://github.com/ks0927/Spoot-Taxi-Socket-Server
- 💻 Api Server: https://github.com/Libienz/Spoot-Taxi-Server

## 📖 프로젝트 개발 배경
- 학생들은 등하교할 때 종종 택시를 이용해야 하는 상황을 마주한다.
- 하지만 택시요금은 대중교통 요금에 비해 상당히 부담되는 수준이며 더욱이 택시 기본요금이 1천원 추가 인상되는 등 학생들이 택시를 이용하기는 더 부담스러워진 상황이다.
- 학생들은 택시 이용 빈도를 줄이기 위해 노력할 수 있겠지만 상명대학교 학생들은 특히 학교 교퉁편의 특수한 상황 때문에 여전히 택시를 필요로 하는 경우가 많다. (ex 시위 등으로 버스 우회)
- 인상된 택시 요금에도 상명대학교 학생들은 여전히 택시 이용을 피할 수 없는 것이다.
- 학생들은 이러한 상황속에서 에브리타임 게시판과 카카오톡 오픈 채팅을 이용하여 동승자를 모집하지만 이는 댓글을 계속 확인하고 오픈채팅방을 새로 개설하여 전파해야 하는 등 번거로운 점이 많다. 
- 이에 본 팀은 인상된 택시요금에도 학생들은 여전히 택시 이용을 피할 수 없다는 점과 기존의 택시 파티를 구하는 과정이 번거로움을 문제로 정의하고 솔루션으로써 택시 동승자 매칭 플랫폼 Spoot Taxi를 개발하였다. 

## 🚂 프로젝트 주요 내용
- 보통 상명대학교 학생들은 택시 동승자를 구하기 위해 다음의 과정을 거친다.
  - 에브리타임에 택시파티를 구한다는 게시글 게시
  - 동승하고자 하는 인원이 댓글 작성
  - 인원 중 한명이 오픈 채팅 단체 채팅방 개설
  - 동승자들끼리 약속 장소를 정하고 만남
  - 택시 호출
- 위 과정은 계속해서 댓글을 확인해야 하며 에브리타임, 카카오톡등 여러 어플리케이션을 옮겨다녀야 해결되는 복잡한 과정이다.
- ### Spoot Taxi는 다음의 기능들을 제공함으로써 이러한 복잡한 과정을 단순화하고 한 곳에 모으는 것에 초점을 두었다!
  - ### 원클릭 매칭
    - 한번의 클릭으로 내 주변 등교행 택시 희망자를 매칭
    - 매칭이 성공할 경우 알림 발생! 번거로운 확인과정 필요 X
  - ![ezgif com-resize](https://github.com/Libienz/Spoot-Taxi-Server/assets/85234650/c7c8f03b-0cf4-4129-b801-5221604a5b07) ![ezgif com-resize (1)](https://github.com/Libienz/Spoot-Taxi-Server/assets/85234650/10c9e46b-09ad-47d4-8250-c90e69d64a48) ![notify](https://github.com/Libienz/Spoot-Taxi-Server/assets/85234650/6f02bfd1-2c81-4619-a98d-45aee92b4193)
  - ### 플랫폼 내 채팅 시스템 및 집회 정보 제공

    - 따로 오픈 채팅방을 팔 필요 없이 어플리케이션 내에서 약속 가능
    - 부가적으로 택시를 이용할지 고민 중인 학생들의 관심사인 집회와 우회정보 제공
  - ![ezgif com-resize (1)](https://github.com/Libienz/Spoot-Taxi-Server/assets/85234650/2397aeb3-c4f9-4f76-8bc5-3b3db3422035) ![rally](https://github.com/Libienz/Spoot-Taxi-Server/assets/85234650/a537cdff-0ed9-45c0-9169-408983aae44e)



## 🛠️ 프로젝트 기술 스택 및 아키텍쳐
![image](https://github.com/Libienz/Spoot-Taxi-Server/assets/85234650/157790b8-37b2-43a1-8639-d89f462dc81b)
### 주요 기술 스택 및 설계 포인트
- #### 소켓 서버와 api 서버의 분리
  -  하나의 서버가 api도 서비스하고 유저들의 채팅을 위한 소켓 커넥션도 유지할 경우 너무 많은 일을 분담
  -  두가지 서버를 나누어 scalable한 설계를 채용
  -  소켓 서버는 소켓 커넥션을 유지하고 유저가 보낸 메시지를 api서버로 전송하는 일만을 담당 (소켓 라이브러리 Stomp 사용)
  -  api 서버는 api를 서비스 하는 일을 집중적으로 담당
- #### 소켓 서버와 api 서버의 작업을 조율하는 Zookeeper와 분산형 데이터 스트리밍을 위한 Kafka
  - 소켓 서버와 api 서버의 통신을 RestApi로 할 수도 있었지만 외부 통신의 병목과 느슨한 결합을 이용할 경우 향상되는 안정성을 고려하여 kafka 채용
- #### 서버 이벤트 푸쉬를 위한 FCM
  - 매칭 성공 알림을 위한 서버 이벤트 푸쉬 기능을 위해 FCM 채용
  - 또한 집회정보 업데이트 시에도 알림을 주어 어플리케이션 접속 빈도를 높일 수 있도록 했음
- #### 인증, 인가 처리를 위한 SpringSecurity + jwt
  - stateless한 처리를 바탕으로 서버 부담을 줄이기 위해 jwt 채용
 

## 🗺️ 엔티티 설계도
![image](https://github.com/Libienz/Spoot-Taxi-Server/assets/85234650/6329202c-cfd0-4602-9bbf-ac4fd013fc7c)

