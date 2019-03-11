package es.lolrav.podsavior.net

import es.lolrav.podsavior.di.module.NetModule
import es.lolrav.podsavior.di.module.NetModule.providesITunesRetrofit
import es.lolrav.podsavior.di.module.NetModule.providesITunesService
import es.lolrav.podsavior.di.module.NetModule.providesMoshi
import es.lolrav.podsavior.di.module.NetModule.providesOkHttpClient
import es.lolrav.podsavior.net.itunes.ITunesService
import org.junit.Before
import org.junit.Test

class ITunesServiceTest {
    lateinit var iTunes: ITunesService

    @Before
    fun setup() {
        iTunes = providesITunesRetrofit(
                providesMoshi(),
                providesOkHttpClient()
        ).let(::providesITunesService)
    }

    @Test
    fun testSearchAmerica() {
        iTunes.search("america")
                .test()
                .also {
                    it.awaitTerminalEvent()
                    it.assertValueCount(1)
                    it.assertValue { results ->
                        results.count > 0
                    }
                    it.assertValue { results ->
                        results.results.any { result ->
                            result.artistName == "Crooked Media"
                        }
                    }
                    it.assertValue { results ->
                        results.results.any { result ->
                            result.name == "Pod Save America"
                        }
                    }
                }
                .dispose()
    }
}