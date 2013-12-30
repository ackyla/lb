class CreateNotifications < ActiveRecord::Migration
  def self.up
    create_table :notifications do |t|
      t.references :user
      t.references :detection
      t.string :notification_type
      t.boolean :delivered, :default => false
      t.boolean :read, :default => false
      t.timestamps
    end
  end

  def self.down
    drop_table :notifications
  end
end
