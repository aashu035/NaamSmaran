# ralph.ps1 — Ralph Loop for Naam Smaran (नाम स्मरण)
# Agentic build-test-verify loop for the v2-native-kotlin project
# जय श्री हित हरिवंश महाप्रभु 🙏
#
# Usage:
#   cd v2-native-kotlin
#   .\scripts\ralph.ps1
#
# The loop reads ralph-tasks.md, picks the first uncompleted task,
# builds the project, runs unit tests, and marks the task done on success.

param(
    [int]$MaxIterations = 20,    # Safety cap to avoid infinite loops
    [switch]$DryRun              # Print tasks without building
)

$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectDir = Split-Path -Parent $scriptDir
$tasksFile  = Join-Path $projectDir "ralph-tasks.md"
$gradlew    = Join-Path $projectDir "gradlew.bat"

# ─── Helpers ──────────────────────────────────────────────────────────────────

function Write-Banner($message) {
    $line = "─" * 60
    Write-Host ""
    Write-Host $line -ForegroundColor Cyan
    Write-Host "  🕉️  $message" -ForegroundColor Cyan
    Write-Host $line -ForegroundColor Cyan
}

function Write-Success($message) { Write-Host "  ✅ $message" -ForegroundColor Green }
function Write-Fail($message)    { Write-Host "  ❌ $message" -ForegroundColor Red }
function Write-Info($message)    { Write-Host "  ℹ️  $message" -ForegroundColor Yellow }

function Get-NextTask {
    if (-not (Test-Path $tasksFile)) {
        Write-Fail "ralph-tasks.md not found at: $tasksFile"
        exit 1
    }
    $lines = Get-Content $tasksFile
    for ($i = 0; $i -lt $lines.Count; $i++) {
        if ($lines[$i] -match "^- \[ \] (.+)$") {
            return @{ Line = $i; Task = $matches[1] }
        }
    }
    return $null
}

function Mark-TaskDone($lineIndex) {
    $lines = Get-Content $tasksFile
    $lines[$lineIndex] = $lines[$lineIndex] -replace "^- \[ \]", "- [x]"
    Set-Content $tasksFile $lines
}

function Invoke-GradleBuild {
    Write-Info "Running: gradlew assembleDebug testDebugUnitTest..."
    $result = & $gradlew assembleDebug testDebugUnitTest 2>&1
    $exitCode = $LASTEXITCODE
    if ($exitCode -ne 0) {
        Write-Fail "Build/test failed (exit $exitCode)"
        Write-Host ($result | Select-String -Pattern "error:|FAILED|Exception" | Select-Object -First 20) -ForegroundColor Red
    }
    return $exitCode
}

# ─── Main Loop ────────────────────────────────────────────────────────────────

Write-Banner "Ralph Loop — Naam Smaran v2-native-kotlin"
Write-Info "Tasks file: $tasksFile"
Write-Info "Max iterations: $MaxIterations"

if ($DryRun) {
    Write-Info "DRY RUN MODE — listing tasks only"
    $content = Get-Content $tasksFile
    $content | ForEach-Object { Write-Host "  $_" }
    exit 0
}

$iteration = 0
$completed = 0

while ($iteration -lt $MaxIterations) {
    $iteration++
    Write-Banner "Iteration $iteration / $MaxIterations"

    # Find next uncompleted task
    $next = Get-NextTask
    if ($null -eq $next) {
        Write-Success "All tasks complete! 🎉 राधे राधे"
        break
    }

    Write-Info "Task: $($next.Task)"

    # Build & test
    $exitCode = Invoke-GradleBuild

    if ($exitCode -eq 0) {
        Mark-TaskDone -lineIndex $next.Line
        $completed++
        Write-Success "Task marked done: $($next.Task)"
    } else {
        Write-Fail "Build failed. Fix the error above, then re-run ralph.ps1."
        Write-Info "Tip: Check app/build/reports/tests/ for unit test details."
        exit 1
    }
}

if ($iteration -ge $MaxIterations) {
    Write-Fail "Max iterations ($MaxIterations) reached without completing all tasks."
    exit 1
}

Write-Banner "Ralph Loop complete. $completed tasks done. राधे राधे 🙏"
