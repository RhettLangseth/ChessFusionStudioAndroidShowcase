param(
    [Parameter(Mandatory = $true)]
    [string]$Name
)

$adb = Join-Path $env:LOCALAPPDATA 'Android\Sdk\platform-tools\adb.exe'
if (-not (Test-Path $adb)) {
    throw "adb not found at $adb"
}

$targetDir = Join-Path $PSScriptRoot 'screenshots'
New-Item -ItemType Directory -Force -Path $targetDir | Out-Null
$targetPath = Join-Path $targetDir ($Name + '.png')

cmd /c "\"$adb\" exec-out screencap -p > \"$targetPath\""
if (-not (Test-Path $targetPath)) {
    throw "Screenshot capture failed."
}

Write-Host "Saved screenshot to $targetPath"
