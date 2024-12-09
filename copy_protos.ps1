# Путь к директории с proto файлами
$protoSrcDir = ".\proto"

# Список модулей
$modules = @("finance-report", "stock-control", "accounting") # Замените на реальные имена модулей

# Копируем proto файлы в каждый модуль
foreach ($module in $modules) {
    $targetDir = "$module\src\main\proto"

    # Создаем директорию, если её нет
    if (!(Test-Path -Path $targetDir)) {
        New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
    }

    # Копируем все .proto файлы
    Copy-Item "$protoSrcDir\*.proto" -Destination $targetDir -Force

    Write-Output "Файлы .proto успешно скопированы в $targetDir"
}