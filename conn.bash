#!/bin/bash

# 원격 서버 정보
REMOTE_USER="sptjoodongho"   # 원격 서버의 사용자
REMOTE_HOST="34.64.173.72"  # 원격 서버의 IP 주소 또는 도메인
# shellcheck disable=SC2088
SSH_KEY="~/.ssh/id_rsa.pub"  # SSH 개인 키 경로

# 접속 시도
echo "SSH 접속을 시도합니다: $REMOTE_USER@$REMOTE_HOST"

if [ -f "$SSH_KEY" ]; then
    ssh -i "$SSH_KEY" "$REMOTE_USER@$REMOTE_HOST"
else
    ssh "$REMOTE_USER@$REMOTE_HOST"
fi

