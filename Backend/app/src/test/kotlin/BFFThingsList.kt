import bff.HOST_URL
import bff.service.things.AddThingRequest
import org.junit.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class BFFThingsList {
    @Test
    fun testListOfThings() {
        val serverURL = HOST_URL + "things/"

        println("=== MAKING CURL REQUEST ===")
        println("URL: $serverURL")
        
        println("==============================")

        try {
            // Build the curl command with proper flags for clean output
            val curlCommand = arrayOf(
                "curl",
                "-s",                           // Silent mode (no progress bar)
                "-S",                           // Show errors even in silent mode
                "-w", "\n--- CURL INFO ---\nHTTP Status: %{http_code}\nTotal Time: %{time_total}s\nResponse Size: %{size_download} bytes\n",
                "-X", "GET",
                "-H", "Content-Type: application/json",
                "-H", "Accept: application/json",
                "--connect-timeout", "10",      // 10 second connection timeout
                "--max-time", "30",             // 30 second total timeout
                "--include",                    // Include response headers
                serverURL
            )

            // Execute the curl command
            val processBuilder = ProcessBuilder(*curlCommand)
            processBuilder.redirectErrorStream(false) // Keep stdout and stderr separate
            val process = processBuilder.start()

            // Read output streams concurrently to prevent blocking
            val outputFuture = CompletableFuture.supplyAsync {
                process.inputStream.bufferedReader().readText()
            }
            val errorFuture = CompletableFuture.supplyAsync {
                process.errorStream.bufferedReader().readText()
            }

            // Wait for process to complete with timeout
            val processCompleted = process.waitFor(35, TimeUnit.SECONDS)

            if (!processCompleted) {
                process.destroyForcibly()
                println("❌ CURL REQUEST TIMED OUT")
                return
            }

            val exitCode = process.exitValue()
            val output = outputFuture.get()
            val errorOutput = errorFuture.get()

            println("=== CURL RESPONSE ===")
            
            if (exitCode == 0) {
                println("✅ Request successful (exit code: $exitCode)")
                
                // Split response body and curl info
                val parts = output.split("\n--- CURL INFO ---\n")
                val fullResponse = parts[0].trim()
                val curlInfo = if (parts.size > 1) parts[1].trim() else ""

                // Since we used --include, we need to separate headers from body
                val headerBodySplit = fullResponse.split("\r\n\r\n", limit = 2)
                val headers = if (headerBodySplit.size > 1) headerBodySplit[0] else ""
                val responseBody = if (headerBodySplit.size > 1) headerBodySplit[1] else fullResponse

                if (headers.isNotEmpty()) {
                    println("📋 Response Headers:")
                    println(headers)
                }

                if (responseBody.isNotEmpty()) {
                    println("📄 Response Body:")
                    println(responseBody)
                } else {
                    println("📄 Response Body: (empty)")
                }

                if (curlInfo.isNotEmpty()) {
                    println("📊 Connection Info:")
                    println(curlInfo)
                }
                
                // Additional debugging for 400 errors
                if (curlInfo.contains("HTTP Status: 400")) {
                    println("🔍 DEBUG: Received 400 Bad Request")
                    println("This usually means:")
                    println("  - JSON format is incorrect")
                    println("  - Missing required fields") 
                    println("  - Server validation failed")
                    println("  - Content-Type header issue")
                }
            } else {
                println("❌ Request failed (exit code: $exitCode)")
                if (output.isNotEmpty()) {
                    println("📄 Output: $output")
                }
            }

            if (errorOutput.isNotEmpty()) {
                println("⚠️  Error Details: $errorOutput")
            }

        } catch (e: Exception) {
            println("❌ Exception occurred: ${e.message}")
            e.printStackTrace()
        }

        println("=====================")
    }
}