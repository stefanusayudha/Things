import bff.HOST_URL
import bff.service.things.AddThingRequest
import org.junit.Test

class BFFThingsAdd {
    @Test
    fun testAddThing() {
        val request = AddThingRequest(
            name = "Thermal01",
            serialNumber = "1111-2222-3333",
            model = "SIMPLE TEMPERATURE",
        )

        val serverURL = HOST_URL + "things/add"

        // Convert the request to JSON format for the POST request
        val jsonBody = """
            {
                "name": "${request.name}",
                "serialNumber": "${request.serialNumber}",
                "model": "${request.model}"
            }
        """.trimIndent()

        println("=== MAKING CURL REQUEST ===")
        println("URL: $serverURL")
        println("JSON Body: $jsonBody")
        println("==============================")

        try {
            // Build the curl command with proper flags for clean output
            val curlCommand = arrayOf(
                "curl",
                "-s",                           // Silent mode (no progress bar)
                "-S",                           // Show errors even in silent mode
                "-w", "\n--- CURL INFO ---\nHTTP Status: %{http_code}\nTotal Time: %{time_total}s\nResponse Size: %{size_download} bytes\n",
                "-X", "POST",
                "-H", "Content-Type: application/json",
                "-H", "Accept: application/json",
                "-d", jsonBody,
                "--connect-timeout", "10",      // 10 second connection timeout
                "--max-time", "30",             // 30 second total timeout
                serverURL
            )

            // Execute the curl command
            val processBuilder = ProcessBuilder(*curlCommand)
            processBuilder.redirectErrorStream(false) // Keep stdout and stderr separate
            val process = processBuilder.start()

            // Read output streams concurrently to prevent blocking
            val outputFuture = java.util.concurrent.CompletableFuture.supplyAsync {
                process.inputStream.bufferedReader().readText()
            }
            val errorFuture = java.util.concurrent.CompletableFuture.supplyAsync {
                process.errorStream.bufferedReader().readText()
            }

            // Wait for process to complete with timeout
            val processCompleted = process.waitFor(35, java.util.concurrent.TimeUnit.SECONDS)

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
                val responseBody = parts[0].trim()
                val curlInfo = if (parts.size > 1) parts[1].trim() else ""

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