@echo off
REM Compile and run WeatherIQ. Optional argument selects the HTTP port (default 8080).
if not exist out mkdir out
javac -d out src\weatheriq\*.java
if errorlevel 1 (
    echo.
    echo Compilation FAILED. Fix the errors above and try again.
    pause
    exit /b 1
)
echo Compilation successful. Starting WeatherIQ...
if "%~1"=="" (
    java -cp out weatheriq.Main
) else (
    java -cp out weatheriq.Main %1
)
pause
