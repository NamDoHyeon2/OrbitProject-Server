#!/bin/bash

# 설정 변수
REMOTE_USER="sptjoodongho"   # 원격 서버의 사용자
REMOTE_HOST="34.64.173.72"  # 원격 서버의 IP 주소 또는 도메인
# shellcheck disable=SC2088
SSH_KEY="~/.ssh/id_rsa"     # 올바른 SSH 개인 키 경로

LOCAL_FILE="./target/project-0.0.1-SNAPSHOT.jar"  # 상대 경로로 설정된 로컬 파일 경로
REMOTE_DIR="/home/dh_joo/BE"    # 원격 서버에 파일을 저장할 디렉토리

# 로컬 파일이 존재하는지 확인
if [[ ! -f "$LOCAL_FILE" ]]; then
    echo "Error: 로컬 파일이 존재하지 않습니다: $LOCAL_FILE"
    exit 1
fi

# SSH 키를 사용하여 원격 서버로 파일 전송
echo "파일을 전송 중: $LOCAL_FILE -> $REMOTE_USER@$REMOTE_HOST:$REMOTE_DIR"
scp -i "$SSH_KEY" "$LOCAL_FILE" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_DIR"

# 전송 결과 확인
if [[ $? -eq 0 ]]; then
    echo "파일 전송 성공!"
else
    echo "파일 전송 실패!"
    exit 1
fi
