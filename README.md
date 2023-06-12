# 게임 컨셉
Puzzle Bobble과 같은 색의 구슬을 충돌 시키는 것으로 점수를 획득하는 게임

같은 색의 구슬이 3개 이상 모일 시 구슬이 파괴 및 점수가 획득되며

일정 시간이 흐를 때마다 최상단에서 구슬 한 줄 생성

이 구슬들이 일정 높이 이상 내려오게 되거나 제한 시간이 모두 소요되면 Game Over


같은 색의 구슬을 일정 개수 이상 한 번에 처리 시 해당 색의 구슬 아이템 생성

이는 아이템 보관함에서 터치하여 장착하거나 합성하여 새로운 아이템으로 생성 가능

# 개발 범위
조작 - 구슬 발사, 아이템 합성(드래그 앤 드롭)

아이템 - 구슬 색상 변경 아이템, 폭탄 아이템, 시간 정지 아이템 총 3가지 구현

UI - 시작 화면, 메인 화면, 아이템 보관함, 일시 정지, 게임 오버 총 5가지 화면 및 UI 구성

![image](https://user-images.githubusercontent.com/89974193/228336831-ade6f809-3f4e-4bee-9bd2-423f902ff8bb.png)
![image](https://user-images.githubusercontent.com/89974193/228336922-ed9b1fa4-55bb-4648-832d-44bd177d15f9.png)


# 개발 일정

![image](https://user-images.githubusercontent.com/89974193/228337029-3429cc55-21bd-4893-8a38-234f0b93a409.png)
![image](https://github.com/chodott/AndroidProgramming/assets/89974193/e49ddd87-7420-4c4d-bde8-c5535e392c63)

1주차 리소스 수집 100%

2주차 구슬 발사 및 충돌 체크 100%

3주차 구슬 충돌 처리 및 점수 기능 구현 80% - 붙어 있음에도 불구하고 떨어지는 구슬 존재

4주차 아이템 구현(구슬, 폭탄, 시간 정지) 100%

5주차 게임 시작 및 게임 오버 UI 구현 100%

6주차 일시 정지 및 아이템 보관함 UI 구현 100%

7주차 테스트 및 난이도 조정 - 80% - 게임 진행에 따른 난이도 변경 없음

8주차 사운드 적용 100%

9주차 디버깅 및 버그 수정 - 50%

# 기술
- 수업 내용에서 차용한 것
Sound, bitmap pool, Score, AnimSprite, Sprite, Metrics, BaseScene, Background

- 사용된 기술
효율적인 알고리즘을 이용하진 않았지만, 구슬을 발사하거나 아이템을 사용할 때 구슬들이 연쇄되어 파괴되거나, 연결된 구슬이 없어 떨어지는 것을 구현하기 위해
HashMap과 재귀함수를 이용하여 구슬을 탐색할 수 있는 기능을 구현하였습니다.

- 직접 개발한 것
- AnimSprite나 Sprite 클래스를 상속받는 게임 상의 객체(Bobble, Item, BombItem, Timer)들과 게임 내의 Bobble들의 충돌 처리와 상호 작용의 역할을 맡는 BobbleManager를 제작하였습니다.

# 아쉬운 것
무엇보다 가장 중요한 게임 로직이 정상 작동하지 않는 점이 가장 아쉽습니다.

게임 플레이 중 시간이 10초 흐를 때마다 가장 윗 줄에서 새로운 구슬들이 내려오는데

이 구슬들을 생성하면서 충돌하고 있는 구슬들 서로의 Key를 저장하게 되는데 

이후 붙어있지 않은 구슬들을 아래로 떨어뜨리는 과정에서 버그가 발생합니다.

# 수업에 대하여
단순히 스마트폰으로 구동 가능한 게임을 제작하는 방법을 기대하면서 수업을 들었으나,
생각지 못했던 화면 모드 변경이나 강제 종료, 등 처리해야 할 예외 부분이이 여럿 있다는 것을
알게 되었습니다.
