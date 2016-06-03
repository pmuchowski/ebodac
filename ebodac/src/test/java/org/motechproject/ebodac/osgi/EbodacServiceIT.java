package org.motechproject.ebodac.osgi;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ebodac.client.EbodacFtpsClient;
import org.motechproject.ebodac.constants.EbodacConstants;
import org.motechproject.ebodac.domain.Config;
import org.motechproject.ebodac.domain.Subject;
import org.motechproject.ebodac.domain.Visit;
import org.motechproject.ebodac.repository.SubjectDataService;
import org.motechproject.ebodac.repository.SubjectEnrollmentsDataService;
import org.motechproject.ebodac.repository.VisitDataService;
import org.motechproject.ebodac.server.FtpsServer;
import org.motechproject.ebodac.service.ConfigService;
import org.motechproject.ebodac.service.EbodacService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class EbodacServiceIT extends BasePaxIT {

    private static final String HOST = "localhost";
    private static final String CSV_DIR = "target/csv/";
    private static final String USER = "test";

    @Inject
    private EbodacService ebodacService;

    @Inject
    private SubjectDataService subjectDataService;

    @Inject
    private SubjectEnrollmentsDataService subjectEnrollmentsDataService;

    @Inject
    private VisitDataService visitDataService;

    @Inject
    private ConfigService configService;

    private FtpsServer ftpsServer = new FtpsServer();
    private EbodacFtpsClient ftpsClient = new EbodacFtpsClient();

    private File csvDir = new File(CSV_DIR);
    private Config savedConfig;

    @Before
    public void setUp() throws Exception {
        ftpsServer.start();
        ftpsClient.connect(HOST, ftpsServer.getPort(), USER, USER);

        savedConfig = configService.getConfig();
        csvDir = new File(CSV_DIR);
        csvDir.mkdirs();
        assertTrue(csvDir.exists());

        visitDataService.deleteAll();
        subjectEnrollmentsDataService.deleteAll();
        subjectDataService.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        ftpsClient.disconnect();
        ftpsServer.stop();

        configService.updateConfig(savedConfig);
        FileUtils.deleteDirectory(csvDir);

        visitDataService.deleteAll();
        subjectEnrollmentsDataService.deleteAll();
        subjectDataService.deleteAll();
    }

    @Test
    public void shouldFetchCSVUpdates() throws Exception {
        Subject subject = new Subject();
        subject.setSubjectId("1000000161");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000355");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000452");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000646");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000258");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000549");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1010004062");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1100006385");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        DateFormat df = new SimpleDateFormat(EbodacConstants.CSV_DATE_FORMAT);
        String filename = CSV_DIR + "motech_" + df.format(new Date()) + ".csv";
        InputStream in = getClass().getResourceAsStream("/sampleRaveFile.csv");
        assertNotNull(in);

        ftpsClient.sendFile(filename, in);
        in.close();

        Config config = configService.getConfig();
        config.setFtpsPort(ftpsServer.getPort());
        config.setFtpsHost(HOST);
        config.setFtpsUsername(USER);
        config.setFtpsPassword(USER);
        config.setFtpsDirectory(CSV_DIR);
        DateTime afterDate = DateTime.now().plusDays(1);
        String lastCsvUpdate = afterDate.toString(EbodacConstants.CSV_DATE_FORMAT);
        config.setLastCsvUpdate(lastCsvUpdate);
        configService.updateConfig(config);

        ebodacService.fetchCSVUpdates();

        List<Visit> visits = visitDataService.retrieveAll();
        assertEquals(0, visits.size());

        afterDate = DateTime.now().minusDays(1);
        lastCsvUpdate = afterDate.toString(EbodacConstants.CSV_DATE_FORMAT);
        config.setLastCsvUpdate(lastCsvUpdate);
        configService.updateConfig(config);

        ebodacService.fetchCSVUpdates();

        visits = visitDataService.retrieveAll();
        assertTrue(visits.size() > 0);
    }

    @Test
    public void shouldFetchCSVFormStartDate() throws Exception {
        Subject subject = new Subject();
        subject.setSubjectId("1000000161");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000355");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000452");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000646");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000258");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000549");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1010004062");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1100006385");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        DateFormat df = new SimpleDateFormat(EbodacConstants.CSV_DATE_FORMAT);
        String filename = CSV_DIR + "motech_" + df.format(new Date()) + ".csv";
        InputStream in = getClass().getResourceAsStream("/sampleRaveFile.csv");
        assertNotNull(in);

        ftpsClient.sendFile(filename, in);
        in.close();

        Config config = configService.getConfig();
        config.setFtpsPort(ftpsServer.getPort());
        config.setFtpsHost(HOST);
        config.setFtpsUsername(USER);
        config.setFtpsPassword(USER);
        config.setFtpsDirectory(CSV_DIR);
        DateTime afterDate = DateTime.now().plusDays(1);
        String lastCsvUpdate = afterDate.toString(EbodacConstants.CSV_DATE_FORMAT);
        config.setLastCsvUpdate(lastCsvUpdate);
        configService.updateConfig(config);

        ebodacService.fetchCSVUpdates(DateTime.now().plusDays(1));

        List<Visit> visits = visitDataService.retrieveAll();
        assertEquals(0, visits.size());

        ebodacService.fetchCSVUpdates(DateTime.now().minusDays(1));

        visits = visitDataService.retrieveAll();
        assertTrue(visits.size() > 0);
    }

    @Test
    public void shouldNotOverridePrimerAndBoosterVaccinationReportsToUpdate() throws Exception {
        Subject subject = new Subject();
        subject.setSubjectId("1000000161");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        subject = new Subject();
        subject.setSubjectId("1000000646");
        subject.setSiteName("siteName");
        subjectDataService.create(subject);

        DateFormat df = new SimpleDateFormat(EbodacConstants.CSV_DATE_FORMAT);
        String filename = CSV_DIR + "motech_" + df.format(new Date()) + ".csv";
        InputStream in = getClass().getResourceAsStream("/raveFileWithBrokenData.csv");
        assertNotNull(in);

        ftpsClient.sendFile(filename, in);
        in.close();

        Config config = configService.getConfig();
        config.setPrimerVaccinationReportsToUpdate(null);
        config.setBoosterVaccinationReportsToUpdate(null);
        config.setLastCalculationDate(LocalDate.now().minusDays(1).toString(DateTimeFormat.forPattern(EbodacConstants.REPORT_DATE_FORMAT)));
        config.setFtpsPort(ftpsServer.getPort());
        config.setFtpsHost(HOST);
        config.setFtpsUsername(USER);
        config.setFtpsPassword(USER);
        config.setFtpsDirectory(CSV_DIR);
        DateTime afterDate = DateTime.now().plusDays(1);
        String lastCsvUpdate = afterDate.toString(EbodacConstants.CSV_DATE_FORMAT);
        config.setLastCsvUpdate(lastCsvUpdate);
        configService.updateConfig(config);

        ebodacService.fetchCSVUpdates(DateTime.now().plusDays(1));

        List<Visit> visits = visitDataService.retrieveAll();
        assertEquals(0, visits.size());

        ebodacService.fetchCSVUpdates(DateTime.now().minusDays(1));

        visits = visitDataService.retrieveAll();
        assertTrue(visits.size() > 0);

        config = configService.getConfig();
        assertEquals(2, config.getPrimerVaccinationReportsToUpdate().size());
        assertEquals(1, config.getBoosterVaccinationReportsToUpdate().size());
    }
}
