class AddIndexToNotifications < ActiveRecord::Migration
  def self.up
    add_index :notifications, :notification_type
  end

  def self.down
    remove_index :notifications, :notification_type
  end
end
