@echo off
echo ===================================================
echo  Starting ELK Stack (Elasticsearch, Logstash, Kibana)
echo ===================================================
docker compose -f docker-compose-elk.yml up -d
echo.
echo ELK Stack starting up:
echo - Elasticsearch: http://localhost:9200
echo - Kibana UI:     http://localhost:5601
echo.
docker compose -f docker-compose-elk.yml ps
pause
