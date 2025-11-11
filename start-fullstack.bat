@echo off
echo ========================================
echo   CityTransit Full Stack - Iniciando
echo ========================================
echo.

cd /d "c:\Users\Camilo Sarmiento\Documents\2025\Software 2 Parcial 1\Nuevo\citytransit-backend"

echo Verificando Docker containers...
docker ps

echo.
echo Iniciando todos los servicios con Docker Compose...
docker-compose up -d

echo.
echo Esperando a que los servicios estén listos...
timeout /t 30 /nobreak

echo.
echo ========================================
echo   Servicios Iniciados
echo ========================================
echo.
echo Backend (Java):      http://localhost:8080
echo Analytics (Python):  http://localhost:8001
echo.
echo API Docs Backend:    http://localhost:8080/swagger-ui.html
echo API Docs Analytics:  http://localhost:8001/docs
echo.
echo GraphQL:             http://localhost:8080/graphql
echo GraphiQL:            http://localhost:8080/graphiql
echo.
echo ========================================

echo.
echo Mostrando logs del Analytics Service...
docker logs -f paytransit-analytics

pause
