package com.singularityuniverse.singularity.android

import EnvironmentProperties
import io.ktor.client.*
import org.koin.dsl.module
import service.authentication.AuthenticationService
import service.authentication.web.AuthenticationWebApiClient
import service.authentication.web.KtorAuthenticationWebApiClient
import utils.defaultHttpClient

val viewModels = module {
}

val services = module {
    single { AuthenticationService(get()) }
}

val dbs = module {
}

val webApis = module {
    single<AuthenticationWebApiClient> { KtorAuthenticationWebApiClient(get()) }
}

val agents = module {
    single<HttpClient> { defaultHttpClient(EnvironmentProperties.WEB_HOST_URL) }
}
