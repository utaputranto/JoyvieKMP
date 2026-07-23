plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.joyvie.spotless)
    alias(libs.plugins.joyvie.sonar)
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.koin.compiler)
}

dependencies {
    subprojects
        .filter { sub ->
            sub.path.startsWith(":core:") ||
                sub.path.startsWith(":feature:") ||
                sub.path == ":composeApp"
        }
        .filter { sub -> sub.subprojects.isEmpty() }
        .forEach { sub ->
            dokka(project(sub.path))
        }
}

abstract class GenerateMasterDokkaIndexTask : DefaultTask() {
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val outputDir = outputDirectory.get().asFile
        outputDir.mkdirs()
        val masterFile = File(outputDir, "index.html")

        val htmlContent =
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>JoyvieKMP Documentation Dashboard</title>
                <style>
                    :root {
                        --bg-color: #0f172a;
                        --card-bg: #1e293b;
                        --accent-color: #38bdf8;
                        --text-color: #f8fafc;
                        --text-muted: #94a3b8;
                        --border-color: #334155;
                    }
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                        background-color: var(--bg-color);
                        color: var(--text-color);
                        margin: 0;
                        padding: 2rem;
                    }
                    .container { max-width: 1200px; margin: 0 auto; }
                    header { margin-bottom: 2.5rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem; }
                    h1 { color: var(--accent-color); font-size: 2.25rem; margin: 0 0 0.5rem 0; }
                    p.subtitle { color: var(--text-muted); font-size: 1.1rem; margin: 0; }
                    .grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 1.5rem; margin-top: 1.5rem; }
                    .card {
                        background: var(--card-bg);
                        border: 1px solid var(--border-color);
                        border-radius: 12px;
                        padding: 1.25rem;
                        transition: transform 0.2s ease, border-color 0.2s ease;
                        display: flex;
                        flex-direction: column;
                        justify-content: space-between;
                    }
                    .card:hover { transform: translateY(-4px); border-color: var(--accent-color); }
                    .badge {
                        display: inline-block;
                        padding: 0.25rem 0.6rem;
                        border-radius: 9999px;
                        font-size: 0.75rem;
                        font-weight: 600;
                        text-transform: uppercase;
                        background: #0369a1;
                        color: #e0f2fe;
                        width: fit-content;
                        margin-bottom: 0.75rem;
                    }
                    .badge.build-logic { background: #7c3aed; color: #f5f3ff; }
                    .badge.core { background: #059669; color: #ecfdf5; }
                    .badge.feature { background: #d97706; color: #fffbeb; }
                    .badge.app { background: #db2777; color: #fdf2f8; }
                    .card h3 { margin: 0 0 0.5rem 0; font-size: 1.25rem; }
                    .card p { color: var(--text-muted); font-size: 0.9rem; margin: 0 0 1rem 0; line-height: 1.4; }
                    .card a {
                        color: var(--accent-color);
                        text-decoration: none;
                        font-weight: 600;
                        font-size: 0.95rem;
                    }
                    .card a:hover { text-decoration: underline; }
                </style>
            </head>
            <body>
                <div class="container">
                    <header>
                        <h1>JoyvieKMP Documentation Dashboard</h1>
                        <p class="subtitle">Unified Kotlin Multiplatform (Android + iOS) & Build Logic Documentation Hub</p>
                    </header>
                    <main>
                        <h2>All Project Modules</h2>
                        <div class="grid">
                            <div class="card">
                                <div>
                                    <span class="badge build-logic">Build Logic</span>
                                    <h3>build-logic:convention</h3>
                                    <p>Gradle convention plugins, KMP target configurations, Dokka V2, Spotless & Sonar rules.</p>
                                </div>
                                <a href="../../../build-logic/convention/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge app">Application</span>
                                    <h3>composeApp</h3>
                                    <p>Compose Multiplatform entry point, navigation backstack host, platform initialization & DI.</p>
                                </div>
                                <a href="../../../composeApp/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge core">Core</span>
                                    <h3>core:model</h3>
                                    <p>Plain data models and domain entities shared across feature modules.</p>
                                </div>
                                <a href="../../../core/model/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge core">Core</span>
                                    <h3>core:network</h3>
                                    <p>Ktor HTTP client setup, TMDB API endpoints, error handling & network data sources.</p>
                                </div>
                                <a href="../../../core/network/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge core">Core</span>
                                    <h3>core:platform</h3>
                                    <p>Expect/actual platform abstractions for logging, device info, and notifications.</p>
                                </div>
                                <a href="../../../core/platform/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge core">Core</span>
                                    <h3>core:datastore</h3>
                                    <p>Jetpack DataStore preferences & Okio local storage bindings.</p>
                                </div>
                                <a href="../../../core/datastore/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge core">Core</span>
                                    <h3>core:designsystem</h3>
                                    <p>Compose Multiplatform UI design system, theme tokens, typography & reusable components.</p>
                                </div>
                                <a href="../../../core/designsystem/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge feature">Feature: Auth</span>
                                    <h3>feature:auth:api</h3>
                                    <p>Auth route keys (`NavKey`) and navigation extension contracts.</p>
                                </div>
                                <a href="../../../feature/auth/api/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge feature">Feature: Auth</span>
                                    <h3>feature:auth:domain</h3>
                                    <p>Auth repositories interfaces and business logic use cases.</p>
                                </div>
                                <a href="../../../feature/auth/domain/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge feature">Feature: Auth</span>
                                    <h3>feature:auth:data</h3>
                                    <p>Auth repository implementations, data sources, and network integration.</p>
                                </div>
                                <a href="../../../feature/auth/data/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge feature">Feature: Auth</span>
                                    <h3>feature:auth:presentation</h3>
                                    <p>Auth Compose UI screens, ViewModels, and navigation entries.</p>
                                </div>
                                <a href="../../../feature/auth/presentation/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge feature">Feature: Onboarding</span>
                                    <h3>feature:onboarding:api</h3>
                                    <p>Onboarding navigation routes and API contracts.</p>
                                </div>
                                <a href="../../../feature/onboarding/api/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge feature">Feature: Onboarding</span>
                                    <h3>feature:onboarding:domain</h3>
                                    <p>Onboarding domain models, repositories, and use cases.</p>
                                </div>
                                <a href="../../../feature/onboarding/domain/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge feature">Feature: Onboarding</span>
                                    <h3>feature:onboarding:data</h3>
                                    <p>Onboarding repository implementation and TMDB movie remote sources.</p>
                                </div>
                                <a href="../../../feature/onboarding/data/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                            <div class="card">
                                <div>
                                    <span class="badge feature">Feature: Onboarding</span>
                                    <h3>feature:onboarding:presentation</h3>
                                    <p>Onboarding Compose UI screens, splash screen, and ViewModels.</p>
                                </div>
                                <a href="../../../feature/onboarding/presentation/build/dokka/html/index.html">View Documentation &rarr;</a>
                            </div>
                        </div>
                    </main>
                </div>
            </body>
            </html>
            """.trimIndent()

        masterFile.writeText(htmlContent)
        println("Generated Master Dokka Index at: \${masterFile.absolutePath}")
    }
}

tasks.register<GenerateMasterDokkaIndexTask>("generateMasterDokkaIndex") {
    group = "documentation"
    description = "Generates a unified master HTML dashboard aggregating documentation for all modules."
    outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
}

tasks.named("dokkaGenerate") {
    finalizedBy("generateMasterDokkaIndex")
}
