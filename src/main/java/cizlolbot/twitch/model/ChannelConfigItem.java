package cizlolbot.twitch.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelConfigItem that = (ChannelConfigItem) o;
        return Objects.equals(channelName, that.channelName) &&
                Objects.equals(command, that.command) &&
                commandTriggerType == that.commandTriggerType &&
                Objects.equals(commandResponseId, that.commandResponseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelName, command, commandTriggerType, commandResponseId);
    }
}
