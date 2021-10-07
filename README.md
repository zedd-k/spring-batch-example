# spring-batch-example
스프링 배치 example

# 과제 목표
스프링 배치 example 코드를 통해 기본적인 job/step의 구성 및 ItemReader/ItemProcess/ItemWriter의 사용법을 습득한다.

# 구현 내용
- reader() : `/src/main/resources/sample-data.csv` 파일을 읽어서 Person 객체로 변환한다.
- processor() : Person의 'name'의 값을 소문자로 변환한다.
- writer() : 변환된 Person 목록을 json 형태로 변환하여 파일로 저장한다. (파일 저장 위치등은 자유)

# example 프로젝트 사용방법
1) repository를 clone
2) 위의 `구현 내용`을 참고하여 `PersonJob.kt` 의 주석으로 설명된 부분을 구현한다.
3) `BatchExerciseApplication.kt` 를 실행시켜서 정상적으로 수행되면 PASS

# 수행 결과 기대값
![결과](https://user-images.githubusercontent.com/89777169/136234505-ecdf7545-c80d-4551-b9bc-be72e7f5aa4c.png)

위의 json 형태를 가진 파일이 생성되었다면 성공.

# 팁
- ItemReader/ItemWriter는 다양한 구현체가 있다. 이것을 활용한다. - [링크](https://docs.spring.io/spring-batch/docs/current/reference/html/readersAndWriters.html)
