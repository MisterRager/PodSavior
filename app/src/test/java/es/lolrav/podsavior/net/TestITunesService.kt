package es.lolrav.podsavior.net

import es.lolrav.podsavior.di.NetModule
import es.lolrav.podsavior.net.itunes.ITunesService
import org.junit.Before
import org.junit.Test

class TestITunesService {
    lateinit var iTunes: ITunesService

    @Before
    fun setup() {
        iTunes = NetModule().let { net ->
            net.providesMoshi()
                    .let(net::providesITunesRetrofit)
                    .let(net::providesITunesService)
        }
    }

    @Test
    fun testSearchAmerica() {
        iTunes.search("america")
                .test()
                .also {
                    it.awaitTerminalEvent()
                    it.assertValue {
                        it.isNotEmpty()
                    }
                }
                .dispose()
    }
}