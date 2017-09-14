# 네이버쇼핑검색기

## 네이버쇼핑검색기 제작 개요

 - 네이버 쇼핑 페이지에 접속하지 않고도, 구매를 원하거나 가격을 검색하고 싶은 상품을 간편하게 검색하기 위함.
 
## 네이버쇼핑검색기 제작 과정

 ### 2017년 9월 5일
  - 네이버 API를 이용하여 JSON 데이터를 받아오고, 이를 RecyclerView 리스트로 뿌려줌.
  - HttpUrlConnection을 이용하여 네이버 서버와 통신함.
  - Gson 라이브러리를 이용하여 API로부터 받은 JSON 데이터를 String 데이터로 파싱함.
  - 파싱한 String 데이터는 별도의 객체 클래스를 만들어 저장함(Singleton 디자인 패턴 사용)
  - RecyclerView를 통해 저장된 데이터를 리스트 형태로 화면에 보여줌.

 ### 2017년 9월 6일
  - 리스트 형태를 격자와 일반 리스트 형태의 두 가지로 선택할 수 있도록 함.
  - 디테일 페이지를 제작하여, 리스트에서 선택한 아이템의 상세 정보를 볼 수 있도록 함.
  - Spinner를 사용하여 검색 결과 노출 시 보여질 아이템 개수를 선택할 수 있도록 함.
  - OnKeyEvent를 이용해 키보드에서 enter키를 누르면 바로 검색 결과를 띄울 수 있도록 함.
  - RecyclerView가 구성될때 LayoutManager에서 Linear와 Grid를 쓴다는 점에 착안하여, 버튼을 누름으로서 리스트와 그리드 형태를 실시간으로 바꿀 수 있도록 함.
  - 리스트에서 특정 아이템을 선택하면 Intent를 통해 Serializable된 정보가 다른 액티비티로 전달됨.
  - 스피너 아이템 등록 시 xml을 이용하지 않고, java 코드에 int 배열을 추가하여 바로 쿼리문에 포함될 수 있도록 함.
  - ThreadLocal을 사용하게 되었는데, 이에 대해서는 좀 더 공부할 필요가 있음.    

### 2017년 9월 7일
  - 리스트에서 아이템 선택 시 선택한 아이템이 아닌 다른 아이템이 디테일 페이지에 나오던 현상 수정
  - 앱 런처 아이콘 제작(일러스트레이터, 네이버 BI 활용) 및 앱 이름 변경(NaverAPI => 네이버쇼핑검색기)
  - 디테일 페이지에서 '사러가기' 버튼 추가, 버튼을 누르면 해당 아이템의 링크로 이동하도록 함
  - UI개선
    > 네이버의 BI 컬러 사용(버튼, 액션바, 상태바 등)
      검색 결과 리스트의 타이틀명 글씨크기 확대, 가격 색상 변경, 최저가격 글씨크기 확대 등 리스트의 UI 개선
  - 검색어를 입력하고 엔터키를 누르거나, '가격 검색' 버튼을 누르면 키보드가 자동으로 사라지도록 로직 구현

### 2017년 9월 8일
  - 리스트 아이템 뷰 크기 조절(한 화면에 더 많은 아이템을 보이기 위해 크기 줄임)
  - LinearLayout과 Switch를 이용하여 메인화면에서 일부 항목을 원하는 때에 보이도록 함
     (검색 시 노출할 아이템 개수, 마지막 검색 시간 등은 상세 설정에 두고 스위치로 Visibility를 조절하도록 함)
  - 메인화면에서 Back 버튼을 눌렀을 때, 바로 종료되지 않고 3초 내에 다시 Back 버튼을 누르면 종료되도록 수정
  - 이슈 : 개인화 기능과 저장을 위해 SQLite를 공부해야 함/Detail 페이지에 활용에 대한 고민 필요./네이버 로고 축소 필요.
  
### 2017년 9월 11일  
  - SQLite에 대한 공부 시작
  - SQLite를 활용하여 검색한 특정 아이템을 데이터베이스에 저장 가능.

### 2017년 9월 12일
  - SQLite를 활용하여 '관심 품목'을 저장하고, 리스트 형태로 보여주는 액티비티 구현
  - SQLite 데이터베이스의 특정/전체 아이템을 삭제하는 기능 구현
  - SQLite 데이터베이스를 활용하여 '관심 품목'의 상세 사항을 보여주는 디테일 액티비티 구현
  - 액션바 상단에 메뉴 구현('관심품목보기', '오픈소스 라이선스', '아이템 삭제')
  - 오픈소스 라이선스에 대한 별도의 액티비티 추가(Gson, Glide, NaverAPI에 대한 라이선스 고지)
  - 디테일 액티비티 UI 개선(SQLite와의 연동 버튼 추가, 최저가와 상품명 표시 개선)
