PROJECT_ROOT="/home/ec2-user/freedom-day-back"
JAR_FILE="$PROJECT_ROOT/freedom-day-back-0.0.1-SNAPSHOT.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

source ~/.bash_profile

echo "$TIME_NOW > $JAR_FILE --spring.profiles.active=dev 실행" >> $DEPLOY_LOG
echo "java -jar $JAR_FILE --spring.profiles.active=dev"
nohup java -jar $JAR_FILE --spring.profiles.active=dev > $APP_LOG 2> $ERROR_LOG &

# 프로세스 아이디가 안나올때
sleep 3

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG

echo "=========================================================================\n"
