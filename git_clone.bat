@echo off
setlocal
 
set "REPOSITORY=https://github.com/jsjolund/GdxDemo3D.git"
set "DIRECTORY=example"
set "MAX_RETRIES=100"
 
:retry
if exist "%DIRECTORY%" rmdir /s /q "%DIRECTORY%"
git clone "%REPOSITORY%" "%DIRECTORY%"
if %ERRORLEVEL% NEQ 0 (
    if %MAX_RETRIES% GTR 0 (
        set /a MAX_RETRIES-=1
        echo 重试中...
        timeout /t 5
        goto retry
    ) else (
        echo 克隆仓库失败。
        exit /b 1
    )
)
 
echo 克隆成功。
endlocal