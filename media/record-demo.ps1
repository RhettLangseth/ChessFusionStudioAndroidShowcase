param(
    [string]$Name = 'demo',
    [int]$Seconds = 12
)

$adb = Join-Path $env:LOCALAPPDATA 'Android\Sdk\platform-tools\adb.exe'
if (-not (Test-Path $adb)) {
    throw "adb not found at $adb"
}

$remotePath = '/sdcard/chessfusionstudio-showcase-demo.mp4'
$targetPath = Join-Path $PSScriptRoot ($Name + '.mp4')

& $adb shell rm -f $remotePath | Out-Null
Write-Host "Recording for up to $Seconds seconds on the connected device..."
& $adb shell screenrecord --time-limit $Seconds $remotePath
& $adb pull $remotePath $targetPath | Out-Null
& $adb shell rm -f $remotePath | Out-Null

if (-not (Test-Path $targetPath)) {
    throw "Demo recording failed."
}

Write-Host "Saved demo recording to $targetPath"
Write-Host "If you want an embeddable README asset, convert the MP4 to GIF after reviewing the clip."
