package com.cheatshqip.integration

import com.cheatshqip.BuildConfig
import com.cheatshqip.adapter.output.ApiBaseURL
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.di.applicationModule
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class GetWordTranslationSuggestionsUseCaseIntegrationTest: KoinTest {
    private val useCase : GetWordTranslationSuggestionsUseCase by inject()
    private val mockWebServer = MockWebServer()

    @BeforeEach
    fun setUp() {
        mockWebServer.start()
        val baseURL = mockWebServer.url("/").toString()
        val baseURLModule = module {
            single { ApiBaseURL(baseURL) }
        }
        startKoin {
            modules(applicationModule, baseURLModule)
        }
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `given some characters, should propose translations`() = runTest {
        val jsonResponse = puneJsonResponse

        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/define/pune" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(jsonResponse)
                        .addHeader("Content-Type", "application/json")

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        val result = useCase.getWorldTranslationSuggestions(Word("work"))

        assertEquals(
            listOf(
                Translation("punë"),
                Translation("pufe"),
                Translation("pure"),
                Translation("arne"),
                Translation("buçe")
            ),
            result
        )
    }

    private val puneJsonResponse = """
                    {
                        "searchedWord": "pune",
                        "exactMatches": [
                            {
                                "word": "punë",
                                "word2": "pune",
                                "word5": "pún/ë",
                                "wordOrig": "sh. -ë(t)",
                                "wordDefinition": "pún/ë,-a <span class=\"SHRT\" data-short=\"f\">emër i gjinisë femërore;</span> <span class=\"SHRT\" data-short=\"sh\">numri shumës;</span> -ë(t) 1. veprimtari e dobishme e njeriut, që e kryen me forcën e krahut e të mendjes dhe me vegla a mjete të ndryshme për të <a class=\"word-link\" href=\"/shndërroj\">shndërruar</a> natyrën, për të prodhuar të mira materiale, për të krijuar vlera shpirtërore etj.; veprimtari e ndërgjegjshme prodhuese, krijuese, organizuese etj.; mënyra si kryhet ajo; lloji i veprimtarisë a detyra që kryen çdo ditë dikush në një fushë të caktuar; lloji i shërbimit që i bëhet diçkaje; mjeshtëri, <a class=\"word-link\" href=\"/zanë\">zanat</a>, profesion: punë <a class=\"word-link\" href=\"/fizik\">fizike</a> (mendore); punë mësimore (edukative); punë e verdhë <span class=\"SHRT\" data-short=\"libr\">i ligjërimit libror;</span> punë që mund të mos sjellë atë që duhet; punë e keqe; punë e ndyrë; punë me spec (bised.) punë e lidhur me vështirësi dhe me kokëçarje e të papritura; në <a class=\"word-link\" href=\"/marrëdhënie\">marrëdhënie</a> pune; ndarja e punës (hist.); shprehitë e punës; koha e punës; <a class=\"word-link\" href=\"/disiplinë\">disiplina</a> e punës; krahët (forcat, veglat, mjetet) e punës; kafshë pune; punët e ditës (e stinës) në bujqësi; dashuria për punën; i ngarkuari me punë (dipl.); iu bë puna shirit (dikujt) mezi e mbaroi një punë, ngaqë nuk i pëlqente a ngaqë mezi e bënte; ka lindur për atë punë e bën shumë mirë një punë, është shumë i përshtatshëm për ta bërë atë; puna e mbarë! (ur.); tryezë (dhomë, rroba) pune; s’ia del njeri për punë është i fortë a i zoti për punë; e vuri punën përpara (nën vete) përparoi një punë; kur s’ke punë, mos luaj derën! mos rri asnjëherë pa punë, diçka duhet të bësh patjetër, sado e vogël qoftë; ajo punë (euf.) <a class=\"word-link\" href=\"/epilepsi\">epilepsia</a>, punëhera; puna e herës (puna e hënës, puna e truallit) (euf.) <a class=\"word-link\" href=\"/epilepsi\">epilepsia</a>, punëhera; punën e sotme mos e lër për nesër (fj. u.); puna e njeh njeriun (fj. u.); puna çan malet (fj. u.). 2. <span class=\"SHRT\" data-short=\"bised\">i ligjërimit bisedor;</span> <a class=\"word-link\" href=\"/ndërmarrje\">ndërmarrja</a>, <a class=\"word-link\" href=\"/institucion\">institucioni</a> etj. ose bashkësia ku punon dikush: shokët e punës; shkoj në punë; u largua (u kthye) nga puna; dhuratë nga puna; ku ke punën, shtro (edhe) gunën (fj. u.). 3. diçka që duhet bërë a duhet zgjidhur, që na shqetëson etj.; çështje; <a class=\"word-link\" href=\"/hall\">hall</a>; <span class=\"SHRT\" data-short=\"bised\">i ligjërimit bisedor;</span> shkak, arsye: punë boshe; punë fëmijësh (<a class=\"word-link\" href=\"/tall\">tall</a>.); ia mbaroi punën (dikujt) e ndihmoi për diçka; si do të vejë kjo punë?; e ndan (e këput, e pret) punën (dikush) i ka gjërat të qarta e të prera, nuk i zvarrit punët; i bie punës prapa e ndjek një çështje derisa të zgjidhet; s’të mbaron punë s’ta kryen një kërkesë, një porosi etj., s’të bën gjë; i ka punët në vijë (për <a class=\"word-link\" href=\"/fill\">fije</a>) i ka punët fare mirë, i vete mbarë; e ka <a class=\"word-link\" href=\"/shkreh\">shkrehur</a> atë punë nuk mendon a nuk shqetësohet më për diçka, ka hequr dorë; s’është puna ime; nuk (më) pret puna duhet të mbarohet, të zgjidhet etj. menjëherë; si qëndron puna?; na hapi punë; t’i vëmë kapak kësaj pune! ta mbyllim këtë çështje; i lë punët <a class=\"word-link\" href=\"/var\">varur</a> (dikush) i zgjat e nuk i zgjidh; s’më shpie (s’më çon) puna atje s’kam të bëj me dikë a me diçka; ua prishi punët u hapi telashe, i pengoi; s’u nisëm dot për punë të borës; punë e pa punë me arsye e pa arsye, me shkak e pa shkak; kësaj i thonë (kjo është) punë e pa punë të bësh punë pa nevojë e pa dobi, t’i hapësh vetes punë kot. 4. lëvizja dhe veprimtaria e një organi, e një aparati, e një mjeti etj.: puna e zemrës (e muskujve); e vë makinën në punë. 5. ajo që arrihet të bëhet gjatë procesit të prodhimit a gjatë një veprimtarie; sendi, vegla etj. që prodhohet; punim: punë shkencore; punë diplome; punë e arrirë; i dorëzoi punën. 6. <span class=\"SHRT\" data-short=\"fiz\">term në fizikë;</span> rezultati i veprimit të një force mbi një trup, e cila shoqërohet me harxhim energjie dhe e zhvendos atë në një drejtim të caktuar: punë <a class=\"word-link\" href=\"/mekanik\">mekanike</a>; njësia e matjes së punës; formula e llogaritjes së punës. 7. gjendja në të cilën ndodhet dikush; <a class=\"word-link\" href=\"/marrëdhënie\">marrëdhëniet</a> me dikë: e ka punën mirë (keq, pisk). 8. <span class=\"SHRT\" data-short=\"bised\">i ligjërimit bisedor;</span> rasti: meqë ra puna; puna e solli... 9. <span class=\"SHRT\" data-short=\"bised\">i ligjërimit bisedor;</span> diçka e ngjashme me një send; lloj, farë: më ka zënë një si punë reumatizme.",
                                "wordDer1": "puna",
                                "wordDer2": "punë",
                                "wordDer3": "punët",
                                "wordDer4": "",
                                "wordDer5": "",
                                "wordDer6": "",
                                "wordDer7": "",
                                "wordDer8": "",
                                "wordDer9": "",
                                "derivedWordMatch": true,
                                "exactMatch": true,
                                "canonicalWord": "punë",
                                "verb": null,
                                "antonyms": [],
                                "synonyms": [
                                    "mjeshtëri",
                                    "profesion",
                                    "punëhera",
                                    "ndërmarrja",
                                    "çështje",
                                    "hall",
                                    "shkak",
                                    "arsye",
                                    "menjëherë",
                                    "sendi",
                                    "punim",
                                    "rasti",
                                    "lloj",
                                    "farë"
                                ]
                            }
                        ],
                        "fuzzyMatches": [
                            {
                                "word": "pufe",
                                "word2": "pufe",
                                "word5": "púf/e",
                                "wordOrig": "sh. -e(t)",
                                "wordDefinition": "púf/e,-ja <span class=\"SHRT\" data-short=\"f\">emër i gjinisë femërore;</span> <span class=\"SHRT\" data-short=\"sh\">numri shumës;</span> -e(t) 1. ndenjëse e ulët dhe e butë si <a class=\"word-link\" href=\"/kolltuk\">kolltuk</a>, pa mbështetëse. 2. copë e vogël cohe me <a class=\"word-link\" href=\"/push\">push</a> për të pudrosur fytyrën.",
                                "wordDer1": "pufja",
                                "wordDer2": "pufe",
                                "wordDer3": "pufet",
                                "wordDer4": "",
                                "wordDer5": "",
                                "wordDer6": "",
                                "wordDer7": "",
                                "wordDer8": "",
                                "wordDer9": "",
                                "derivedWordMatch": false,
                                "exactMatch": false,
                                "canonicalWord": "pufe",
                                "verb": null,
                                "antonyms": [],
                                "synonyms": []
                            },
                            {
                                "word": "pure",
                                "word2": "pure",
                                "word5": "puré",
                                "wordOrig": "",
                                "wordDefinition": "puré,-ja <span class=\"SHRT\" data-short=\"f\">emër i gjinisë femërore;</span> qull i trashë me <a class=\"word-link\" href=\"/perime\">perime</a> ose me pemë të shtypura; <a class=\"word-link\" href=\"/gjellë\">gjellë</a> me qull të tillë patatesh, me qumësht, me <a class=\"word-link\" href=\"/gjalpë\">gjalpë</a> etj.",
                                "wordDer1": "pureja",
                                "wordDer2": "",
                                "wordDer3": "",
                                "wordDer4": "",
                                "wordDer5": "",
                                "wordDer6": "",
                                "wordDer7": "",
                                "wordDer8": "",
                                "wordDer9": "",
                                "derivedWordMatch": false,
                                "exactMatch": false,
                                "canonicalWord": "pure",
                                "verb": null,
                                "antonyms": [],
                                "synonyms": []
                            },
                            {
                                "word": "arne",
                                "word2": "arne",
                                "word5": "arné",
                                "wordOrig": "sh. -nj(të)",
                                "wordDefinition": "arné,-ni <span class=\"SHRT\" data-short=\"m\">emër i gjinisë mashkullore;</span> <span class=\"SHRT\" data-short=\"sh\">numri shumës;</span> -nj(të) <span class=\"SHRT\" data-short=\"bot\">term në botanikë;</span> pishë malore, me <a class=\"word-link\" href=\"/halë\">hala</a> të vogla e të radhitura pesë e nga pesë; keqinë: arneni i zi; arneni i bardhë.",
                                "wordDer1": "arneni",
                                "wordDer2": "arnenj",
                                "wordDer3": "arnenjtë",
                                "wordDer4": "",
                                "wordDer5": "",
                                "wordDer6": "",
                                "wordDer7": "",
                                "wordDer8": "",
                                "wordDer9": "",
                                "derivedWordMatch": false,
                                "exactMatch": false,
                                "canonicalWord": "arne",
                                "verb": null,
                                "antonyms": [],
                                "synonyms": [
                                    "keqinë"
                                ]
                            },
                            {
                                "word": "buçe",
                                "word2": "buce",
                                "word5": "búç/e",
                                "wordOrig": "sh. -e(t) bised.",
                                "wordDefinition": "búç/e,-ja <span class=\"SHRT\" data-short=\"f\">emër i gjinisë femërore;</span> <span class=\"SHRT\" data-short=\"sh\">numri shumës;</span> -e(t) <span class=\"SHRT\" data-short=\"bised\">i ligjërimit bisedor;</span> 1. <a class=\"word-link\" href=\"/bushtër\">bushtër</a>. 2. <span class=\"SHRT\" data-short=\"keq\">me kuptim keqësues;</span> gojë: mbylle buçen!",
                                "wordDer1": "buçja",
                                "wordDer2": "buçe",
                                "wordDer3": "buçet",
                                "wordDer4": "",
                                "wordDer5": "",
                                "wordDer6": "",
                                "wordDer7": "",
                                "wordDer8": "",
                                "wordDer9": "",
                                "derivedWordMatch": false,
                                "exactMatch": false,
                                "canonicalWord": "buçe",
                                "verb": null,
                                "antonyms": [],
                                "synonyms": [
                                    "bushtër",
                                    "gojë"
                                ]
                            },
                            {
                                "word": "bufe",
                                "word2": "bufe",
                                "word5": "bufé",
                                "wordOrig": "sh. -(të)",
                                "wordDefinition": "bufé,-ja <span class=\"SHRT\" data-short=\"f\">emër i gjinisë femërore;</span> <span class=\"SHRT\" data-short=\"sh\">numri shumës;</span> -(të) 1. <a class=\"word-link\" href=\"/dollap\">dollap</a> a <a class=\"word-link\" href=\"/raft\">raft</a> për enë ose për ushqime. 2. pritje me pije, me <a class=\"word-link\" href=\"/ëmbëlsirë\">ëmbëlsira</a> etj.",
                                "wordDer1": "bufeja",
                                "wordDer2": "bufe",
                                "wordDer3": "bufetë",
                                "wordDer4": "",
                                "wordDer5": "",
                                "wordDer6": "",
                                "wordDer7": "",
                                "wordDer8": "",
                                "wordDer9": "",
                                "derivedWordMatch": false,
                                "exactMatch": false,
                                "canonicalWord": "bufe",
                                "verb": null,
                                "antonyms": [],
                                "synonyms": []
                            },
                            {
                                "word": "bung",
                                "word2": "bung",
                                "word5": "bun/g",
                                "wordOrig": "sh. -gje(t)",
                                "wordDefinition": "bun/g,-gu <span class=\"SHRT\" data-short=\"m\">emër i gjinisë mashkullore;</span> <span class=\"SHRT\" data-short=\"sh\">numri shumës;</span> -gje(t) [búng/ë,-a I <span class=\"SHRT\" data-short=\"f\">emër i gjinisë femërore;</span> <span class=\"SHRT\" data-short=\"sh\">numri shumës;</span> -a(t)] <span class=\"SHRT\" data-short=\"bot\">term në botanikë;</span> lloj <a class=\"word-link\" href=\"/dushk\">dushku</a> i lartë e i trashë, me kurorë të gjerë, që bën lende <a class=\"word-link\" href=\"/tufë\">tufë</a> në një bisht: bunga e butë bungëbutë.",
                                "wordDer1": "bungu",
                                "wordDer2": "buna",
                                "wordDer3": "bungje",
                                "wordDer4": "bungjet",
                                "wordDer5": "bunat",
                                "wordDer6": "",
                                "wordDer7": "",
                                "wordDer8": "",
                                "wordDer9": "",
                                "derivedWordMatch": false,
                                "exactMatch": false,
                                "canonicalWord": "bung",
                                "verb": null,
                                "antonyms": [],
                                "synonyms": []
                            },
                            {
                                "word": "buqe",
                                "word2": "buqe",
                                "word5": "búq/e",
                                "wordOrig": "sh. -e(t)",
                                "wordDefinition": "búq/e,-ja <span class=\"SHRT\" data-short=\"f\">emër i gjinisë femërore;</span> <span class=\"SHRT\" data-short=\"sh\">numri shumës;</span> -e(t) <a class=\"word-link\" href=\"/bushtër\">bushtër</a>.",
                                "wordDer1": "buqja",
                                "wordDer2": "buqe",
                                "wordDer3": "buqet",
                                "wordDer4": "",
                                "wordDer5": "",
                                "wordDer6": "",
                                "wordDer7": "",
                                "wordDer8": "",
                                "wordDer9": "",
                                "derivedWordMatch": false,
                                "exactMatch": false,
                                "canonicalWord": "buqe",
                                "verb": null,
                                "antonyms": [],
                                "synonyms": []
                            },
                            {
                                "word": "cule",
                                "word2": "cule",
                                "word5": "cúl/e",
                                "wordOrig": "kryes. sh. -e(t)",
                                "wordDefinition": "cúl/e,-ja I <span class=\"SHRT\" data-short=\"f\">emër i gjinisë femërore;</span> <span class=\"SHRT\" data-short=\"kryes\">kryesisht;</span> <span class=\"SHRT\" data-short=\"sh\">numri shumës;</span> -e(t) shtresat e mbulesat për të fjetur; <a class=\"word-link\" href=\"/zhele\">zhele</a>: culet e foshnjës shpërgënjtë; ishte veshur me cule.",
                                "wordDer1": "culja",
                                "wordDer2": "cule",
                                "wordDer3": "culet",
                                "wordDer4": "",
                                "wordDer5": "",
                                "wordDer6": "",
                                "wordDer7": "",
                                "wordDer8": "",
                                "wordDer9": "",
                                "derivedWordMatch": false,
                                "exactMatch": false,
                                "canonicalWord": "cule",
                                "verb": null,
                                "antonyms": [],
                                "synonyms": [
                                    "zhele",
                                    "kërci"
                                ]
                            },
                            {
                                "word": "cule",
                                "word2": "cule",
                                "word5": "cúl/e",
                                "wordOrig": "sh. -e(t)",
                                "wordDefinition": "cúl/e,-ja II <span class=\"SHRT\" data-short=\"f\">emër i gjinisë femërore;</span> <span class=\"SHRT\" data-short=\"sh\">numri shumës;</span> -e(t) <span class=\"SHRT\" data-short=\"anat\">term në anatomi;</span> <a class=\"word-link\" href=\"/kërci\">kërci</a>, <a class=\"word-link\" href=\"/fyell\">fyelli</a> i këmbës.",
                                "wordDer1": "culja",
                                "wordDer2": "cule",
                                "wordDer3": "culet",
                                "wordDer4": "",
                                "wordDer5": "",
                                "wordDer6": "",
                                "wordDer7": "",
                                "wordDer8": "",
                                "wordDer9": "",
                                "derivedWordMatch": false,
                                "exactMatch": false,
                                "canonicalWord": "cule",
                                "verb": null,
                                "antonyms": [],
                                "synonyms": [
                                    "zhele",
                                    "kërci"
                                ]
                            }
                        ],
                        "firstCanonicalWord": null
                    }
                """.trimIndent()
}