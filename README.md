# API 문서 (API Documentation)

## 1. 개요 (Overview)
이 문서는 **OneBite API** 프로젝트의 아키텍처 및 제공하는 API 엔드포인트에 대한 상세 명세를 다룹니다.
이 프로젝트는 학습 콘텐츠(Content)와 카테고리(Category) 정보를 제공하는 백엔드 서비스로, **AWS Lambda** 환경에서 실행되도록 구성된 **Serverless Spring Boot** 애플리케이션입니다.

### 기술 스택 (Tech Stack)
- **Language:** Java 21
- **Framework:** Spring Boot 3.2.5
- **Build Tool:** Gradle (Kotlin DSL)
- **Deployment:** AWS Lambda (Serverless Java Container)
- **Database:** PostgreSQL (Production), H2 (Local/Test)

---

## 2. 아키텍처 (Architecture)
이 프로젝트는 관심사의 분리를 위해 **계층형 아키텍처 (Layered Architecture)** 를 따르고 있습니다.
패키지 구조는 `dev.onebite.api` 하위에 다음과 같이 구성되어 있습니다.

### 2.1 계층 구조 (Layer Structure)

| 계층 (Layer) | 패키지명 (`package`) | 설명 (Description) |
|:---:|:---:|:---|
| **Presentation** | `presentation` | 외부 요청(HTTP)을 받아 처리하고 응답을 반환합니다. <br> - `api`: REST Controller (`CategoryApiController`, `LearningDataApiController`) <br> - `dto`: 요청/응답 객체 (Request/Response DTO) |
| **Application** | `application` | 비즈니스 로직을 수행하며 도메인과 인프라 계층을 연결합니다. <br> - `service`: 핵심 비즈니스 로직 구현 (`CategoryService`, `ContentService`) |
| **Domain** | `domain` | 핵심 비즈니스 도메인 엔티티를 정의합니다. <br> - `Content`, `Category`, `CategoryGroup` 등 |
| **Infrastructure** | `infra` | 데이터베이스 접근 및 외부 시스템과의 연동을 담당합니다. <br> - `repository`: JPA Repository <br> - `config`: 설정 파일 <br> - `enums`: 공통 열거형 타입 |

### 2.2 배포 구조 (Deployment)
- `aws-serverless-java-container-springboot3` 라이브러리를 사용하여 Spring Boot 애플리케이션을 AWS Lambda 핸들러로 래핑합니다.
- **ShadowJar** 플러그인을 통해 모든 의존성을 포함한 Fat Jar(AWS 배포용)를 생성합니다.

---

## 3. API 명세 (API Specification)

### 3.1 카테고리 API (Category API)
**Base URL:** `/`

#### 3.1.1 전체 카테고리 조회 (Get All Categories)
카테고리 그룹 및 하위 카테고리 목록을 계층 구조로 조회합니다.

- **URL:** `/categories`
- **Method:** `GET`
- **Description:** 카테고리 그룹별로 묶인 전체 카테고리 목록과 메타데이터(총 개수 등)를 반환합니다.

**Response**
- **Status:** `200 OK`
- **Body:**
```json
{
  "success": true,
  "data": {
    "groups": [
      {
        "groupLabel": "string", // 그룹명 (예: CS 지식)
        "groupKey": "string",   // 그룹 키 (예: cs)
        "icon": "string",       // 아이콘 URL 또는 식별자
        "categories": [
          {
            "label": "string",  // 카테고리명 (예: 운영체제)
            "key": "string",    // 카테고리 키 (예: os)
            "icon": "string",   // 아이콘
            "count": 0          // 해당 카테고리의 콘텐츠 수
          }
        ]
      }
    ],
    "totalCategories": 0, // 전체 카테고리 수
    "totalContent": 0     // 전체 콘텐츠 수
  }
}
```

---

### 3.2 학습 데이터 API (Learning Data API)
**Base URL:** `/`

#### 3.2.1 콘텐츠 조회 (Get Content)
조건에 맞는 학습 콘텐츠를 조회합니다. 커서 기반 페이지네이션(Cursor-based Pagination)을 지원합니다.

- **URL:** `/content`
- **Method:** `GET`
- **Description:** 카테고리 필터링 및 랜덤 시드(Seed)를 기반으로 콘텐츠 목록을 반환합니다.

**Request Parameters**
| Parameter | Type | Required | Description |
|:---:|:---:|:---:|:---|
| `limit` | `int` | **Yes** | 조회할 콘텐츠 개수 (Min: 1, Max: 100) |
| `categories` | `String` | No | 필터링할 카테고리 키 목록 (콤마로 구분, 예: `os,network`) |
| `lastSeenId` | `Long` | No | 마지막으로 조회한 콘텐츠 ID (페이지네이션용) |
| `seed` | `String` | No | 랜덤 정렬을 위한 시드값 (일관된 랜덤 순서 보장) |

**Response**
- **Status:** `200 OK`
- **Body:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 0,
        "type": "code_tip | bug_challenge | code_review | meme | interview",
        "title": "string",
        "tags": ["string"],
        "createdAt": "2023-10-27T10:00:00"
        // 콘텐츠 타입에 따라 추가 필드가 존재할 수 있음
      }
    ],
    "lastSeenId": 0, // 다음 페이지 조회를 위한 커서 ID
    "hasNext": true  // 다음 페이지 존재 여부
  }
}
```

