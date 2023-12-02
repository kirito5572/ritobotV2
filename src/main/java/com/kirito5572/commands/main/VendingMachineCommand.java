package com.kirito5572.commands.main;

import com.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VendingMachineCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        List<String> productList = new ArrayList<>();
        productList.add("데자와");
        productList.add("실론티");
        productList.add("맥콜");
        productList.add("솔의눈");
        productList.add("보드카");
        productList.add("웰치스 포도");
        productList.add("웰치스 딸기");
        productList.add("웰치스 청포도");
        productList.add("데미소다 사과");
        productList.add("데미소다 복숭아");
        productList.add("환타 오렌지");
        productList.add("환타 파인애플");
        productList.add("환타 포도");
        productList.add("환타 스무디 복숭아");
        productList.add("환타 딸기");
        productList.add("닥터 페퍼");
        productList.add("코카 콜라");
        productList.add("코카 콜라 제로");
        productList.add("펩시");
        productList.add("펩시 제로 라임");
        productList.add("펩시 제로 망고");
        productList.add("레드불");
        productList.add("몬스터 에너지");
        productList.add("핫식스");
        productList.add("BLIN");
        productList.add("젝다니엘");
        productList.add("오란씨 레몬");
        productList.add("오란씨 오렌지");
        productList.add("오란씨 파인애플");
        productList.add("써니텐 파인애플");
        productList.add("써니텐 오렌지");
        productList.add("써니텐 포도");
        productList.add("칠성 사이다");
        productList.add("킨 사이다");
        productList.add("스프라이트");
        productList.add("제주 삼다수");
        productList.add("지코");
        productList.add("비타코코");
        productList.add("비타500");
        productList.add("미린다");
        productList.add("밀키스");
        productList.add("암바사");
        productList.add("마운틴듀");
        productList.add("이프로");
        productList.add("게토레이");
        productList.add("파워에이드");
        productList.add("오라떼");
        productList.add("후레쉬레몬");
        productList.add("레몬에이드");
        productList.add("봉봉");
        productList.add("갈아만든배");
        productList.add("알로에농장");
        productList.add("모메존");

        Random random = new Random();
        int randomIndex = random.nextInt(productList.size());

        event.reply(productList.get(randomIndex)).setEphemeral(true).queue();
    }

    @Override
    public String getHelp() {
        return "자판기에서 음료를 뽑아봅시다!\n" +
                "뭐가 나올지는 저도 장담 못해드립니다.";
    }

    @Override
    public String getInvoke() {
        return "자판기";
    }
}
