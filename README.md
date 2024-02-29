# 스타터밸리 API

![project](https://github.com/inhalin/starter-valley-back/assets/71866731/1534c99a-619c-488f-ac0f-bb844442851c)

## 프로젝트 개요

부트캠프 수강생 및 운영진을 위한 출결관리 및 커뮤니티 웹 서비스입니다.

### 기획 의도

- 수강생들은 기존의 번거로운 출석체크 방식 대신 GPS 기반으로 원클릭으로 출석체크를 손쉽게 해결합니다. 또한 본인 기수의 수강생들과 자유롭게 소통하며 친목을 다질 수 있습니다.
- 운영진은 모든 수강생들의 출결 관리와 중도 포기자 관리, 공지 등록, 문의 확인 등 부트캠프 관리를 위한 여러가지 업무를 처리할 수 있습니다.
- 실제 수강생의 약 80%가 매일 출석 체크 기능을 사용하였습니다.

### 프로젝트 관리

- 팀장으로서 프로젝트의 전체적인 기획을 주도하고 배포 일정 등의 개발 스케줄을 관리하였습니다.
- 프로젝트에 애자일 방식을 적용하여 2주 간격으로 스프린트와 배포를 진행하였습니다.
- 데일리 미팅, 주간 회고, 스프린트 회고를 통해 팀 내 이슈나 개발 진행 사항을 공유하였습니다.
- 깃과 깃허브로 코드를 관리하고 PR을 통해 코드 리뷰를 진행하였습니다.
- 팀원들과 노션, 슬랙, 피그마 등의 협업 툴을 사용하여 소통하였습니다.
- API 명세서, 기능 명세서, 깃허브 전략, 트러블 슈팅 내용을 문서화하여 공유하였습니다.

## 개발 상세 내용

### 사용 기술

- API 개발: Java, Spring boot, JPA, Querydsl
- 데이터베이스: MariaDB
- 코드 형상 관리: Git, Github
- 클라우드: AWS EC2, AWS RDS

### 담당 개발 기능

- 회원가입 및 로그인
    - 기수별로 지정된 코드를 입력하여 회원가입
    - 깃허브 OAuth2와 JWT를 이용한 로그인
- 런치버스(점심팟, 간식팟 등 소모임)
    - 모임 생성자는 등록, 수정, 삭제 가능
    - 다른 유저들은 모임에 합류, 하차 가능
    - 마감된 모임은 최대 3일 이내의 목록만 조회 가능
- 커뮤니티
    - 수강생 정보
        - 본인 기수의 전체 수강생 목록 조회
        - 본인 정보 수정
    - 문의 게시판
        - 익명 또는 실명으로 개발자, 운영진애게 문의 작성 가능
        - 추후 문의 내역 조회시 실명으로 작성한 문의만 조회 가능
- 백오피스
    - 기수별로 금일 출석 현황 통계를 확인
    - 전체 수강생의 출결관리, 출석 상태 변경 및 관리자 메모
    - 중도 포기자 등록
    - 공지사항 등록, 수정, 삭제
    - 문의 게시판에 등록된 글 조회
- 슬랙 알림
    - 런치버스, 공지사항, 개발자 문의글 등록시 슬랙 알림 연동

## API 목록 

- 실제 담당하여 개발한 API 목록입니다.

### 프론트오피스

#### 로그인/로그아웃

| Method | Endpoint                               | 상세 설명           |
|--------|----------------------------------------|--------------------|
| POST   | /api/auth/login | 로그인 |
| POST   | /api/auth/token/refresh | access token 재발급 |
| POST   | /api/auth/logout | 로그아웃 |
| POST   | /api/auth/signup | 회원가입 |
| POST   | /api/auth/signup/validate | 회원가입시 코드 검증 |
| GET    | /api/auth/available/generations | 기수 목록 |
| GET    | /api/auth/available/devparts?generation={generationId} | 기수별 파트목록 |

#### 런치버스

| Method | Endpoint                               | 상세 설명           |
|--------|----------------------------------------|--------------------|
| GET    | /api/lunchbuses | 모임 전체 조회 |
| GET    | /api/lunchbuses/past | 모집 종료된 모임 목록 |
| POST   | /api/lunchpuses | 모임 등록 |
| GET    | /api/lunchbuses/{id} | 모임 단건 조회 |
| POST   | /api/lunchpuses/{id}/join | 모임 합류 |
| POST   | /api/lunchpuses/{id}/leave | 모임 나가기 |
| DELETE | /api/lunchpuses/{id} | 모임 삭제 |
| POST   | /api/lunchpuses/{id}/close | 모임 참여자 모집 종료 |

#### 커뮤니티

수강생정보, 공지사항, 문의게시판

| Method | Endpoint                               | 상세 설명           |
|--------|----------------------------------------|--------------------|
| GET    | /api/starters | 본인 기수 유저 전체 목록 조회 |
| GET    | /api/starters/{username} | 유저 정보 단건 조회 |
| PUT    | /api/starters/{username} | 유저 본인 정보 수정 |
| GET    | /api/generation/parts | 본인 기수의 개발파트 전체 조회 |
| GET    | /api/notices?page={number}&size={number}&sort={string}&dir={string} | 공지사항 목록 조회 |
| GET    | /api/notices | 공지사항 단건 조회 |
| POST   | /api/inquiries | 문의게시판 글 등록 |
| GET    | /api/inquiries | 문의게시판 목록 조회 |
| GET    | /api/inquiries/{id} | 문의게시판 단건 조회 |


### 백오피스

#### 로그인/로그아웃

| Method | Endpoint                               | 상세 설명           |
|--------|----------------------------------------|--------------------|
| POST   | /api/admin/auth/login | 관리자 로그인 |
| POST   | /api/admin/auth/token/refresh | 관리자 access token 재발급 |
| POST   | /api/admin/auth/logout | 관리자 로그아웃 |
| POST   | /api/admin/auth/register | 관리자 등록 |
| POST   | /api/admin/auth/me/password | 본인 비밀번호 변경 |
| GET    | /api/admin/managers | 관리자 목록 조회 |
| DELETE | /api/admin/managers/{id} | 관리자 삭제 |

#### 대시보드

| Method | Endpoint                               | 상세 설명           |
|--------|----------------------------------------|--------------------|
| GET    | /api/admin/dashboards/statistics/attendance | 오늘의 출석체크 현황 |

#### 출석 관리

| Method | Endpoint                               | 상세 설명           |
|--------|----------------------------------------|--------------------|
| GET    | /api/admin/attendances | 전체 수강생 출결 정보 조회 |
| GET    | /api/admin/attendances/{userId} | 특정 수강생 출결 정보 조회 |
| GET    | /api/admin/attendances?userId={userId}&date={yyyy-MM-dd} | 특정 수강생 특정 날짜 출결 정보 조회 |
| POST   | /api/admin/attendances/{id} | 특정 수강생 특정 날짜 출결 정보 수정 |

#### 유저관리

| Method | Endpoint                               | 상세 설명           |
|--------|----------------------------------------|--------------------|
| GET    | /api/admin/users | 수강생 생성 |
| POST   | /api/admin/users | 수강생 등록 |
| GET    | /api/admin/users/{userId} | 수강생 단건 조회 |
| PUT    | /api/admin/users/{userId} | 수강생 정보 수정 |
| GET    | /api/admin/users/{userId}/attedance?date={yyyy-MM-dd} | 수강생 출석일별 상세 조회 |
| PUT    | /api/admin/users/{userId}/attedance | 유저 출석상태 수정 |
| DELETE | /api/admin/users/{userId} | 유저 삭제 |
| POST   | /api/admin/users/dropouts/{userId} | 중도하차생 등록 |
| GET    | /api/admin/users/dropouts | 중도하차생 전체 조회 |
| GET    | /api/admin/users/dropouts/{userId} | 중도하차생 단건 조회 |
| PUT    | /api/admin/users/dropouts/{userId} | 중도하차생 정보 수정 |

#### 기수 관리

| Method | Endpoint                               | 상세 설명           |
|--------|----------------------------------------|--------------------|
| GET    | /api/admin/generations | 기수 전체 조회 |
| POST   | /api/admin/generations | 기수/파트 등록 |
| GET    | /api/admin/generations/{id} | 기수 단건 조회 |
| PUT    | /api/admin/generations/{id} | 기수 단건 수정 |
| DELETE | /api/admin/generations/{id} | 기수 삭제 |
| PUT    | /api/admin/generations/{id}/devparts | 해당 기수의 파트 수정 |
| DELETE | /api/admin/generations/{id}/devparts | 해당 기수의 파트 삭제 |
| POST   | /api/admin/generations/{id}/devparts | 해당 기수의 파트 추가 |

#### 커뮤니티 관리

공지사항, 문의게시판

| Method | Endpoint                               | 상세 설명           |
|--------|----------------------------------------|--------------------|
| GET    | /api/admin/notices?page={number}&size={number}&sort={string}&dir={string} | 공지사항 전체 조회 |
| GET    | /api/admin/notices/{id} | 공지사항 단건 조회 |
| PUT    | /api/admin/notices/{id} | 공지사항 수정 |
| POST   | /api/admin/notices | 공지사항 글 등록 |
| DELETE | /api/admin/notices/{id} | 공지사항 삭제 |
| GET    | /api/admin/inquiries | 문의글 전체 조회 |
| GET    | /api/admin/inquiries/{id} | 문의글 단건 조회 |
| DELETE | /api/admin/inquiries/{id} | 문의글 삭제 |
