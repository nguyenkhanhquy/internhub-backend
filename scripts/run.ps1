# Đảm bảo PowerShell dùng UTF-8
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Đang thiết lập mã hóa UTF-8 cho PowerShell..." -ForegroundColor Yellow
chcp 65001
Write-Host "Mã hóa đã được thiết lập thành UTF-8." -ForegroundColor Green


# Hàm kiểm tra file và thông báo lỗi nếu không tồn tại
function Check-FileExists($filePath, $errorMessage) {
    if (-Not (Test-Path $filePath)) {
        Write-Host $errorMessage -ForegroundColor Red
        exit 1
    }
}

# Kiểm tra sự tồn tại của các file cần thiết
Check-FileExists ".env.local" "File .env.local không tồn tại. Vui lòng kiểm tra lại."
Check-FileExists "pom.xml" "File pom.xml không tồn tại. Vui lòng kiểm tra lại."
Check-FileExists "src/main/resources/application.yaml" "File application.yaml không tồn tại. Vui lòng kiểm tra lại."

# Đọc file .env.local và thiết lập biến môi trường
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Đang đọc file .env.local và thiết lập biến môi trường..." -ForegroundColor Yellow
Get-Content .env.local | ForEach-Object {
    if ($_ -match "^\s*#") { return }  # Bỏ qua dòng comment
    if ($_ -match "^\s*$") { return }  # Bỏ qua dòng trống
    $parts = $_ -split '=', 2 # Tách tên và giá trị
    $name = $parts[0].Trim() # Tên biến
    $value = $parts[1].Trim() # Giá trị biến
    [System.Environment]::SetEnvironmentVariable($name, $value, "Process") # Thiết lập biến môi trường cho tiến trình hiện tại
    # Write-Host "Đã thiết lập biến môi trường: $name = $value" # In ra thông báo
}
Write-Host "Đã thiết lập tất cả biến môi trường từ file .env.local." -ForegroundColor Green

# Kiểm tra các công cụ cần thiết
function Check-CommandExists($command, $toolName) {
    if (-Not (Get-Command $command -ErrorAction SilentlyContinue)) {
        Write-Host "$toolName không được cài đặt. Vui lòng cài đặt $toolName trước khi chạy ứng dụng." -ForegroundColor Red
        exit 1
    }
}

# Kiểm tra Java, Maven
Check-CommandExists "java" "Java"
Check-CommandExists "mvn" "Maven"
Check-CommandExists "mysql" "MySQL"

Write-Host "=====================================" -ForegroundColor Cyan
# Kiểm tra phiên bản Java có phải là 21 trở lên không
$javaVersion = & java -version 2>&1 | Select-String -Pattern 'version "(\d+)' | ForEach-Object { $_.Matches.Groups[1].Value }
if ([int]$javaVersion -lt 21) {
    Write-Host "Java phiên bản $javaVersion không đủ yêu cầu. Vui lòng cài đặt Java 21 trở lên." -ForegroundColor Red
    exit 1
}
Write-Host "Java phiên bản $javaVersion đã được phát hiện." -ForegroundColor Green

# Kiểm tra xem Maven có phiên bản 3.9.9 trở lên không
$mavenVersion = & mvn -v | Select-String -Pattern 'Apache Maven (\d+\.\d+\.\d+)' | ForEach-Object { $_.Matches.Groups[1].Value }
if ([version]$mavenVersion -lt [version]"3.9.9") {
    Write-Host "Maven phiên bản $mavenVersion không đủ yêu cầu. Vui lòng cài đặt Maven 3.9.9 trở lên." -ForegroundColor Red
    exit 1
}
Write-Host "Maven phiên bản $mavenVersion đã được phát hiện." -ForegroundColor Green

# Chạy ứng dụng với Maven
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Bắt đầu chạy ứng dụng..." -ForegroundColor Yellow
mvn spring-boot:run
