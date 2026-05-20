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
$remotePath = "/sdcard/$Name.png"

& $adb shell screencap -p $remotePath | Out-Null
& $adb pull $remotePath $targetPath | Out-Null
& $adb shell rm -f $remotePath | Out-Null

if (-not (Test-Path $targetPath)) {
    throw "Screenshot capture failed."
}

Write-Host "Saved screenshot to $targetPath"
