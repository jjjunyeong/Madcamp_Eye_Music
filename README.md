1. 프로젝트 이름: 눈으로 듣는 음악
2. 팀원: 박준영, 장종원

3. 프로젝트 설명: 3개의 탭을 가진 앱을 개발. 각 탭 별로 세부 설명을 추가.

||Loading Tab||
  
  3-1: PhoneBook Tab
    SQLite 폰 내장 데이터베이스를 사용해서 전화번호와 이름을 저장하고 리사이클러뷰에 나타냈습니다.
    아이템을 꾹 눌러 리스트에서 삭제가 가능하며, 우하단 + 버튼을 통해서 연락처 추가가 가능합니다.
    상단의 검색란을 통해 원하는 연락처를 이름을 통해 검색할 수 있습니다.
    
![20210706_212811](https://user-images.githubusercontent.com/54852021/124605340-c3738f80-dea6-11eb-8cca-c24e2031d059.gif) ![20210706_213140](https://user-images.githubusercontent.com/54852021/124605462-e1d98b00-dea6-11eb-873a-905ee3683985.gif)

  
    
  3-2: Gallery Tab
    GridView를 이용하여 drawable folder의 사진들을 크기에 맞춰 불러왔습니다.
    사진을 클릭하면 전체 화면으로 볼 수 있고, 핀치 줌을 이용해 확대 축소가 가능합니다.
    사진을 길게 클릭하면 사진의 우측 상단에 하트 표시를 통해 '좋아요'기능을 구현했습니다.
    
    
![ezgif com-gif-maker](https://user-images.githubusercontent.com/54852021/124608255-8957bd00-dea9-11eb-9eec-cba5e8e5a23e.gif)
    
    
    
  3-3: Music Visualizer Tab
    Sample Title 버튼을 클릭하면 내장되어 있는 음악 리스트를 보고 원하는 음악을 선택할 수 있습니다.
    음악이 선택 된 상태에서 재생 버튼을 클릭하면 음악이 재생됩니다.
    재생 상태에서는 재생 버튼이 일시정지 버튼으로 바뀌게 됩니다.
    재생 버튼의 오른쪽은 시각화 버튼으로 이를 클릭하면 하단의 이미지 뷰에 시각화된 소리가 그려지게 됩니다.
    해당 화면은 소리를 푸리에 변환하여 주파수 평면으로 바꾼 후 주파수를 옥타브 별로 변환하여 낮은 음은 푸르게, 높은 음은 붉게 색칠하여 나타냈으며,
    큰 소리 일수록 큰 원으로 그려지게 됩니다.
    시각화 버튼을 누르는 순간 자동으로 화면이 녹화 되며, 다시 한 번 버튼을 누르는 순간까지 녹화가 진행됩니다.
    녹화가 끝난 후에는 그 오른 쪽의 목록 버튼을 클릭하여 갤러리에 저장된 영상을 로딩할 수 있습니다.
    
    
   ![20210706_213859](https://user-images.githubusercontent.com/54852021/124604522-fb2e0780-dea5-11eb-9c04-8887b0c426b9.gif) ![20210706_214152](https://user-images.githubusercontent.com/54852021/124604661-1d278a00-dea6-11eb-8596-6a8e83ee8e22.gif)
   
   
   
    

