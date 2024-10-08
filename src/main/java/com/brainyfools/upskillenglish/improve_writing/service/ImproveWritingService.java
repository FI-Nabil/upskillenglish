package com.brainyfools.upskillenglish.improve_writing.service;

import com.brainyfools.upskillenglish.gemini.GeminiService;
import com.brainyfools.upskillenglish.improve_writing.model.ImproveWritingForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ImproveWritingService {

    GeminiService geminiService;

    public ImproveWritingService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public ResponseEntity<?> judgeWriting(String question, String answer) {
        String prompt = String.format("""
                Judge the answer according to the question: "%s". The answer is: "%s".
                
                Please present your evaluation in the following JSON format, don't add any extra field:
                {
                    "solutionList": [
                        {
                            "solution": "String"
                        }
                    ]
                }
                
                Formating Guidelines:
                - List each feedback as separate items in `solutionList`.
                - Avoid using placeholders like asterisks '*' in response.
                
                Please list all errors with their solutions, specifying the sentence for each error. Provide overall feedback on whether the writing answers the question.
                For solutions, correct grammar issues (verb tense, subject-verb agreement, articles, prepositions), check spelling and punctuation, and assess coherence and organization.
                Ensure the essay addresses both views, supports the writer's opinion, uses varied vocabulary, simplifies complex sentences, and must include check of meeting the minimum word count (250 words).
                """, question, answer);

        ImproveWritingForm improveWritingForm = geminiService.call(prompt, ImproveWritingForm.class);
        return new ResponseEntity<>(improveWritingForm, HttpStatus.CREATED);
    }
}
