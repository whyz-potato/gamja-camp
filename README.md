# ⛺감자 캠핑⛺


## 🙋🏻팀 소개

| <img src="https://avatars.githubusercontent.com/u/59015764?v=4" width="80">  | <img src="https://avatars.githubusercontent.com/u/83811925?v=4" width="80">  | <img src="https://avatars.githubusercontent.com/u/68414987?v=4" width="80">  |
|:----------------------------------------------------------------------------:|:----------------------------------------------------------------------------:|:----------------------------------------------------------------------------:|
|                      [김다슬](https://github.com/Daseull)                       |                      [황정아](https://github.com/jaqkqh15)                      |                      [허서영](https://github.com/hs03130)                       |
|                                   Back-end                                   |                                  Front-end                                   |                                   Back-end                                   |


## 👋🏻프로젝트 소개

안녕하세요 Team Potato 입니다.

*감자 캠핑*은 캠핑족을 위한 사이트 입니다.

전국의 **캠핑장 검색과 예약** 그리고 관심사를 공유할 수 있는 **커뮤니티 기능**을 제공합니다.

*감자 캠핑*에서 마음이 맞는 동반자를 찾아 캠핑을 떠나보세요.


## 🚀기술

![vuejs](https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vue.js&logoColor=4FC08D)

![springboot](https://img.shields.io/badge/Spring_boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![aws](https://img.shields.io/badge/Amazon_AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)
![springsecurity](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)

![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)


## 시작 가이드

### Requirements

- Vue.js 2.6.14
- vue/cli 5.0.8
- vuetify 2.6.0


- JAVA 15.0.2
- H2 database
- Oauth : Google, Naver, KaKao
- AWS S3 bucket 생성

### Installation

**Frontend**

**H2**

**Oauth**


**AWS S3**

`backend/src/main/resources/application-aws.properties`
```properties
cloud.aws.s3.bucket={bucket}
cloud.aws.stack.auto=false
cloud.aws.region.static={region.static}
cloud.aws.credentials.access-key={access-key}
cloud.aws.credentials.secret-key={secret-key}
```

## 🖥️화면 구성
((사진추가))
- 메인
- 예약
- 커뮤니티
- 채팅
- 기업 페이지

## 🏁주요 기능
### 캠핑장 검색 및 예약
- 캠핑장 검색
- 캠핑장 예약
- 캠핑장 리뷰 작성

### 캠핑 관련 커뮤니티
- 자유 게시판
- 캠핑 동반자 모집 게시판
- 채팅

### 사업자 관련 기능
- 캠핑장 등록 및 관리
- 예약 관리 및 1:1 채팅

## 🛠️ERD
![감자캠핑.png](backend/domain_final.png)
## 🛠️API Docs
[Gamja Camp API Docs](https://documenter.getpostman.com/view/19596204/2s93m4X319)
