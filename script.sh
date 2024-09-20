WORKSPACE=$HOME/workspace/
# PROJETO="single-tenant"
# PROJETO="multi-tenant-id"
# PROJETO="multi-tenant-schema"
PROJETO="multi-tenant-hybrid"

function rodar {
    docker compose -f $WORKSPACE/$1/docker-compose-local.yml up -d
    sleep 5;
    bash $WORKSPACE/stress-test-gatling/run-test.sh
    sleep 2;
    docker compose -f $WORKSPACE/$1/docker-compose-local.yml down
    sleep 3;
}

rodar $PROJETO
# rodar $PROJETO
# rodar $PROJETO
# rodar $PROJETO
# rodar $PROJETO