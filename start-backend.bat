@echo off
echo ========================================
echo   CityTransit Backend - Iniciando
echo ========================================
echo.

REM Configurar variables de entorno
set JAVA_HOME=C:\Users\Camilo Sarmiento\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\java\21
set PATH=%JAVA_HOME%\bin;%PATH%

REM Cambiar al directorio del proyecto
cd /d "%~dp0"

echo Verificando Docker containers...
docker ps

echo.
echo Iniciando aplicación Spring Boot...
echo.

REM Ejecutar con Java directamente (ruta corta)
"%JAVA_HOME%\bin\java.exe" ^
  -Xms512m ^
  -Xmx2048m ^
  -Dspring.profiles.active=dev ^
  -Dfile.encoding=UTF-8 ^
  -jar target\citytransit-backend-1.0.0.jar

pause
