package com.one_id_nepal.resource_server.config.init_db;

import com.one_id_nepal.resource_server.citizenship.entity.Citizenship;
import com.one_id_nepal.resource_server.nid.entity.NationalId;
import com.one_id_nepal.resource_server.person.entity.Person;
import com.one_id_nepal.resource_server.person.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialDataConfig {

    private final PersonRepository personRepository;

    // Helper record to hold diverse, realistic Nepali test data
    private record NpProfile(
            String firstEn, String midEn, String lastEn,
            String firstNp, String midNp, String lastNp,
            String gender, String dob, String bloodGroup, String marital,
            String fatherEn, String fatherNp,
            String motherEn, String motherNp,
            String province, String district, String municipality, String ward
    ) {}

    @PostConstruct
    public void initData() {
        List<String> userIds = Arrays.asList(
                "1e7f3c4a-9b5a-4c2f-8123-111111111111",
                "2a8d4b5c-6c7d-4e8f-9123-222222222222",
                "3b9e5c6d-7d8e-4f9a-a123-333333333333",
                "4c0f6d7e-8e9f-40ab-b123-444444444444",
                "5d1a7e8f-9f0a-41bc-c123-555555555555",
                "6e2b8f90-0a1b-42cd-d123-666666666666",
                "7f3c9012-1b2c-43de-e123-777777777777",
                "809da123-2c3d-44ef-f123-888888888888",
                "91aeb234-3d4e-4501-0123-999999999999",
                "a2bfc345-4e5f-4612-1123-aaaaaaaaaaaa",
                "b3c0d456-5f60-4723-2123-bbbbbbbbbbbb",
                "c4d1e567-6071-4834-3123-cccccccccccc",
                "d5e2f678-7182-4945-4123-dddddddddddd",
                "e6f3a789-8293-4a56-5123-eeeeeeeeeeee",
                "f704b89a-93a4-4b67-6123-ffffffffffff",
                "08a5c9ab-a4b5-4c78-7123-111122223333",
                "19b6da9c-b5c6-4d89-8123-222233334444",
                "2ac7ebad-c6d7-4e9a-9123-333344445555",
                "3bd8fcbf-d7e8-40ab-a123-444455556666",
                "4ce9fdc0-e8f9-41bc-b123-555566667777",
                "5df0aec1-f901-42cd-c123-666677778888",
                "6e01bfd2-0a12-43de-d123-777788889999"
        );

        // 10 distinct profiles covering different regions, ethnicities, and genders of Nepal
        List<NpProfile> profiles = Arrays.asList(
                new NpProfile("Aarav", "Prasad", "Sharma", "आरभ", "प्रसाद", "शर्मा", "Male", "1995-05-14", "O+", "Single", "Ram Prasad Sharma", "राम प्रसाद शर्मा", "Sita Devi Sharma", "सीता देवी शर्मा", "Bagmati", "Kathmandu", "Kathmandu Metropolitan", "10"),
                new NpProfile("Sunita", "", "Maharjan", "सुनिता", "", "महर्जन", "Female", "1998-11-22", "A+", "Married", "Bhairav Maharjan", "भैरव महर्जन", "Laxmi Maharjan", "लक्ष्मी महर्जन", "Bagmati", "Lalitpur", "Lalitpur Metropolitan", "15"),
                new NpProfile("Rakesh", "Kumar", "Yadav", "राकेश", "कुमार", "यादव", "Male", "1990-02-10", "B+", "Married", "Shyam Sundar Yadav", "श्याम सुन्दर यादव", "Anita Devi Yadav", "अनिता देवी यादव", "Madhesh", "Dhanusha", "Janakpur Sub-Metropolitan", "4"),
                new NpProfile("Pasang", "Lhamu", "Sherpa", "पासाङ", "ल्हामु", "शेर्पा", "Female", "2001-08-30", "O-", "Single", "Dorje Sherpa", "दोर्जे शेर्पा", "Mingma Sherpa", "मिङ्मा शेर्पा", "Koshi", "Solukhumbu", "Solu Dudhkunda", "2"),
                new NpProfile("Bikash", "", "Tharu", "विकास", "", "थारु", "Male", "1993-12-05", "AB+", "Single", "Ramdin Tharu", "रामदिन थारु", "Kamala Tharu", "कमला थारु", "Lumbini", "Banke", "Nepalgunj Sub-Metropolitan", "8"),
                new NpProfile("Sumnima", "", "Rai", "सुम्निमा", "", "राई", "Female", "1997-04-18", "A-", "Married", "Kiran Rai", "किरण राई", "Parbati Rai", "पार्वती राई", "Koshi", "Ilam", "Ilam Municipality", "5"),
                new NpProfile("Ramesh", "Bahadur", "Karki", "रमेश", "बहादुर", "कार्की", "Male", "1988-09-25", "O+", "Married", "Bhim Bahadur Karki", "भिम बहादुर कार्की", "Radha Karki", "राधा कार्की", "Karnali", "Surkhet", "Birendranagar", "6"),
                new NpProfile("Bishnu", "Maya", "Kami", "विष्णु", "माया", "कामी", "Female", "1992-07-12", "B-", "Divorced", "Hira Lal Kami", "हिरा लाल कामी", "Tulasi Maya Kami", "तुलसी माया कामी", "Gandaki", "Kaski", "Pokhara Metropolitan", "17"),
                new NpProfile("Abdul", "Rahman", "Ansari", "अब्दुल", "रहमान", "अन्सारी", "Male", "1996-03-08", "A+", "Single", "Mohammad Ansari", "मोहमद अन्सारी", "Fatima Khatun", "फातिमा खातुन", "Lumbini", "Kapilvastu", "Taulihawa", "3"),
                new NpProfile("Kritika", "", "Shrestha", "कृतिका", "", "श्रेष्ठ", "Female", "2003-01-20", "B+", "Single", "Sanjeev Shrestha", "संजिव श्रेष्ठ", "Anju Shrestha", "अन्जु श्रेष्ठ", "Bagmati", "Bhaktapur", "Bhaktapur Municipality", "9")
        );

        String defaultMaleAvatar = "https://static.vecteezy.com/system/resources/thumbnails/048/216/761/small/modern-male-avatar-with-black-hair-and-hoodie-illustration-free-png.png";
        String defaultFemaleAvatar = "https://cdn3d.iconscout.com/3d/premium/thumb/woman-avatar-6299541-5187873.png";

        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.get(i);

            if (personRepository.findByUserId(userId).isPresent()) {
                continue;
            }

            // Loop through the 10 realistic profiles
            NpProfile p = profiles.get(i % profiles.size());

            Person person = new Person();
            person.setUserId(userId);

            // Name (English)
            person.setFirstName(p.firstEn());
            person.setMiddleName(p.midEn());
            person.setLastName(p.lastEn());

            // Name (Nepali)
            person.setNepaliFirstName(p.firstNp());
            person.setNepaliMiddleName(p.midNp());
            person.setNepaliLastName(p.lastNp());

            // Demographics
            person.setDateOfBirth(p.dob());
            person.setGender(p.gender());
            person.setBloodGroup(p.bloodGroup());
            person.setMartialStatus(p.marital()); // Keeping your exact method name
            person.setNationality("Nepali");
            person.setProfilePhoto(p.gender().equals("Male") ? defaultMaleAvatar : defaultFemaleAvatar);

            // Parents (English & Nepali)
            person.setFatherName(p.fatherEn());
            person.setNepaliFatherName(p.fatherNp());
            person.setMotherName(p.motherEn());
            person.setNepaliMotherName(p.motherNp());

            // Permanent Address
            person.setProvince(p.province());
            person.setDistrict(p.district());
            person.setMunicipality(p.municipality());
            person.setWardNo(p.ward());

            // Temporary Address (Simulating urban migration for some users)
            boolean migrated = i % 2 == 0;
            person.setTemporaryProvince(migrated ? "Bagmati" : p.province());
            person.setTemporaryDistrict(migrated ? "Kathmandu" : p.district());
            person.setTemporaryMunicipality(migrated ? "Kathmandu Metropolitan" : p.municipality());
            person.setTemporaryWardNo(migrated ? "10" : p.ward());

            person.setStatus(true);

            // Create and associate Citizenship (Realistic Formats)
            Citizenship citizenship = new Citizenship();
            citizenship.setCitizenshipId("CIT-" + userId);
            // e.g., KAS-27-01-79-1234
            String formattedCitNo = p.district().substring(0, 3).toUpperCase() + "-27-01-79-" + (1000 + i);
            citizenship.setCitizenshipNumber(formattedCitNo);
            citizenship.setIssuedDate("2015-04-1" + (i % 9)); // Randomize day a bit
            citizenship.setIssuedDistrict(p.district());
            citizenship.setPerson(person);

            // Create and associate NationalId (Realistic Formats)
            NationalId nationalId = new NationalId();
            nationalId.setNID("NID-" + userId);
            // e.g., 1004-5678-9012
            nationalId.setNidNumber(String.format("100%d-%04d-%04d", i % 9, 1000 + i, 5000 + i));
            nationalId.setFatherNidNumber(String.format("100%d-%04d-%04d", i % 9, 2000 + i, 6000 + i));
            nationalId.setFatherCitizenshipNumber("CIT-F-" + (8000 + i));
            nationalId.setMotherNidNumber(String.format("100%d-%04d-%04d", i % 9, 3000 + i, 7000 + i));
            nationalId.setMotherCitizenshipNumber("CIT-M-" + (9000 + i));
            nationalId.setPerson(person);

            person.setCitizenship(citizenship);
            person.setNid(nationalId);

            personRepository.save(person);
        }
    }
}
