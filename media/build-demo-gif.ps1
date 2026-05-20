Add-Type -AssemblyName System.Drawing

function New-PropertyItem {
    param(
        [int]$Id,
        [int]$Type,
        [byte[]]$Value
    )

    $item = [System.Runtime.Serialization.FormatterServices]::GetUninitializedObject([System.Drawing.Imaging.PropertyItem])
    $item.Id = $Id
    $item.Type = $Type
    $item.Len = $Value.Length
    $item.Value = $Value
    return $item
}

$root = Split-Path $PSScriptRoot -Parent
$frames = @(
    (Join-Path $root 'media\screenshots\home.png'),
    (Join-Path $root 'media\screenshots\board-theme.png'),
    (Join-Path $root 'media\screenshots\light-square-picker.png'),
    (Join-Path $root 'media\screenshots\piece-theme.png')
)
$output = Join-Path $root 'media\demo.gif'

foreach ($frame in $frames) {
    if (-not (Test-Path $frame)) {
        throw "Missing screenshot: $frame"
    }
}

$images = $frames | ForEach-Object { [System.Drawing.Image]::FromFile($_) }
try {
    $gifEncoder = [System.Drawing.Imaging.ImageCodecInfo]::GetImageEncoders() | Where-Object { $_.MimeType -eq 'image/gif' }
    if (-not $gifEncoder) {
        throw 'GIF encoder not found.'
    }

    $frameDelayBytes = New-Object byte[] ($images.Count * 4)
    $delays = @(120, 120, 140, 140)
    for ($i = 0; $i -lt $images.Count; $i++) {
        $bytes = [BitConverter]::GetBytes([int]$delays[$i])
        [Array]::Copy($bytes, 0, $frameDelayBytes, $i * 4, 4)
    }

    $loopBytes = [byte[]](0, 0)
    $delayItem = New-PropertyItem -Id 0x5100 -Type 4 -Value $frameDelayBytes
    $loopItem = New-PropertyItem -Id 0x5101 -Type 3 -Value $loopBytes

    $first = $images[0]
    $first.SetPropertyItem($delayItem)
    $first.SetPropertyItem($loopItem)

    $encoder = [System.Drawing.Imaging.Encoder]::SaveFlag
    $encoderParameters = New-Object System.Drawing.Imaging.EncoderParameters(1)
    $encoderParameters.Param[0] = New-Object System.Drawing.Imaging.EncoderParameter($encoder, [long][System.Drawing.Imaging.EncoderValue]::MultiFrame)
    $first.Save($output, $gifEncoder, $encoderParameters)

    for ($i = 1; $i -lt $images.Count; $i++) {
        $images[$i].SetPropertyItem($delayItem)
        $images[$i].SetPropertyItem($loopItem)
        $encoderParameters.Param[0] = New-Object System.Drawing.Imaging.EncoderParameter($encoder, [long][System.Drawing.Imaging.EncoderValue]::FrameDimensionTime)
        $first.SaveAdd($images[$i], $encoderParameters)
    }

    $encoderParameters.Param[0] = New-Object System.Drawing.Imaging.EncoderParameter($encoder, [long][System.Drawing.Imaging.EncoderValue]::Flush)
    $first.SaveAdd($encoderParameters)
    Write-Host "Saved demo GIF to $output"
}
finally {
    foreach ($image in $images) {
        $image.Dispose()
    }
}
