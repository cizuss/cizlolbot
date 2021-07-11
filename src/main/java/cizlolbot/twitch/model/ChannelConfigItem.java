package cizlolbot.twitch.model;

public class ChannelConfigItem {
    private String channelName;
    private String command;
    private CommandTriggerType commandTriggerType;
    private String commandResponseId;

    public String getChannelName() {
        return channelName;
    }

    public ChannelConfigItem setChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public String getCommand() {
        return command;
    }

    public ChannelConfigItem setCommand(String command) {
        this.command = command;
        return this;
    }

    public String getCommandResponseId() {
        return commandResponseId;
    }

    public ChannelConfigItem setCommandResponseId(String commandResponseId) {
        this.commandResponseId = commandResponseId;
        return this;
    }

    public CommandTriggerType getCommandTriggerType() {
        return commandTriggerType;
    }

    public ChannelConfigItem setCommandTriggerType(CommandTriggerType commandTriggerType) {
        this.commandTriggerType = commandTriggerType;
        return this;
    }
}
