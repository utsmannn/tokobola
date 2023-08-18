package com.utsman.tokobola.api

import com.utsman.tokobola.network.NetworkSources

abstract class WebDataSource : NetworkSources(BuildKonfig.BASE_URL)